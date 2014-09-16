/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.persistence.repository.event;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.support.FileReferencePropertyHandler;
import org.lightadmin.core.storage.FileResourceStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import java.io.IOException;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FileManipulationRepositoryEventListener extends ManagedRepositoryEventListener {

    private static final Logger logger = LoggerFactory.getLogger(FileManipulationRepositoryEventListener.class);

    private final ThreadLocal<FileReferenceProperties> fileReferencePropertiesContext = newFileItemsContext();

    private final FileResourceStorage fileResourceStorage;

    public FileManipulationRepositoryEventListener(GlobalAdministrationConfiguration configuration, FileResourceStorage fileResourceStorage) {
        super(configuration);
        this.fileResourceStorage = fileResourceStorage;
    }

    @Override
    protected void onBeforeCreate(Object entity) {
        onBeforeSave(entity);
    }

    @Override
    protected void onAfterCreate(final Object entity) {
        onAfterSave(entity);
    }

    @Override
    protected void onBeforeSave(Object entity) {
        PersistentEntity persistentEntity = persistentEntityFor(entity.getClass());

        NotEmptyFileReferencePropertiesCollector propertyValueCollector = new NotEmptyFileReferencePropertiesCollector(entity);
        persistentEntity.doWithProperties(propertyValueCollector);

        FileReferenceProperties fileReferenceProperties = propertyValueCollector.getFilePropertyValues();

        persistentEntity.doWithProperties(new FileReferencePropertiesValueEraser(entity, fileReferenceProperties));

        fileReferencePropertiesContext.set(fileReferenceProperties);
    }

    @Override
    protected void onAfterSave(Object entity) {
        PersistentEntity persistentEntity = persistentEntityFor(entity.getClass());
        DynamicJpaRepository repository = repositoryFor(entity.getClass());

        FileReferenceProperties fileReferenceProperties = fileReferencePropertiesContext.get();
        try {
            persistentEntity.doWithProperties(new FileReferencePropertiesSaveHandler(entity, fileReferenceProperties));

            repository.save(entity);
        } finally {
            fileReferencePropertiesContext.remove();
        }
    }

    @Override
    protected void onAfterDelete(final Object entity) {
        PersistentEntity persistentEntity = persistentEntityFor(entity.getClass());

        persistentEntity.doWithProperties(new PersistentPropertyFileDeletionHandler(entity));
    }

    private class PersistentPropertyFileDeletionHandler extends FileReferencePropertyHandler {
        private final Object entity;

        public PersistentPropertyFileDeletionHandler(Object entity) {
            this.entity = entity;
        }

        @Override
        protected void doWithPersistentPropertyInternal(PersistentProperty<?> property) {
            fileResourceStorage.delete(entity, property);
        }
    }

    private static class NotEmptyFileReferencePropertiesCollector extends FileReferencePropertyHandler {
        private final BeanWrapper beanWrapper;
        private final FileReferenceProperties fileReferenceProperties;

        private NotEmptyFileReferencePropertiesCollector(Object instance) {
            this.beanWrapper = new DirectFieldAccessFallbackBeanWrapper(instance);
            this.fileReferenceProperties = new FileReferenceProperties();
        }

        @Override
        protected void doWithPersistentPropertyInternal(PersistentProperty<?> property) {
            String propertyName = property.getName();
            String propertyValue = (String) beanWrapper.getPropertyValue(propertyName);
            if (isNotBlank(propertyValue)) {
                fileReferenceProperties.addPropertyValue(propertyName, propertyValue.getBytes());
            }
        }

        public FileReferenceProperties getFilePropertyValues() {
            return fileReferenceProperties;
        }
    }

    private class FileReferencePropertiesSaveHandler extends FileReferencePropertyHandler {
        private final Object entity;
        private final FileReferenceProperties fileReferenceProperties;

        public FileReferencePropertiesSaveHandler(Object entity, FileReferenceProperties fileReferenceProperties) {
            this.entity = entity;
            this.fileReferenceProperties = fileReferenceProperties;
        }

        @Override
        protected void doWithPersistentPropertyInternal(PersistentProperty<?> property) {
            try {
                if (this.fileReferenceProperties.contains(property.getName())) {
                    byte[] propertyValue = this.fileReferenceProperties.getPropertyValue(property.getName());
                    fileResourceStorage.save(entity, property, propertyValue);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class FileReferencePropertiesValueEraser extends FileReferencePropertyHandler {

        private final BeanWrapper beanWrapper;
        private final FileReferenceProperties fileReferenceProperties;

        private FileReferencePropertiesValueEraser(Object instance, FileReferenceProperties fileReferenceProperties) {
            this.beanWrapper = new DirectFieldAccessFallbackBeanWrapper(instance);
            this.fileReferenceProperties = fileReferenceProperties;
        }

        @Override
        protected void doWithPersistentPropertyInternal(PersistentProperty<?> property) {
            if (fileReferenceProperties.contains(property.getName())) {
                beanWrapper.setPropertyValue(property.getName(), "NULL_VALUE");
            }
        }
    }

    private static class FileReferenceProperties {
        private Map<String, byte[]> filePropertyValues = newLinkedHashMap();

        public void addPropertyValue(String propertyName, byte[] propertyValue) {
            this.filePropertyValues.put(propertyName, propertyValue);
        }

        public byte[] getPropertyValue(String propertyName) {
            return this.filePropertyValues.get(propertyName);
        }

        public boolean contains(String propertyName) {
            return filePropertyValues.containsKey(propertyName);
        }
    }

    private static ThreadLocal<FileReferenceProperties> newFileItemsContext() {
        return new ThreadLocal<FileReferenceProperties>() {
            protected FileReferenceProperties initialValue() {
                return new FileReferenceProperties();
            }
        };
    }
}