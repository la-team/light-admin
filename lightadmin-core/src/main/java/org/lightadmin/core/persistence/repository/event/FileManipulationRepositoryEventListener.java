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

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.lightadmin.core.storage.FileResourceStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

public class FileManipulationRepositoryEventListener extends ManagedRepositoryEventListener {

    private static final Logger logger = LoggerFactory.getLogger(FileManipulationRepositoryEventListener.class);

    private final FileResourceStorage fileResourceStorage;

    public FileManipulationRepositoryEventListener(GlobalAdministrationConfiguration configuration, FileResourceStorage fileResourceStorage) {
        super(configuration);
        this.fileResourceStorage = fileResourceStorage;
    }

    @Override
    protected void onBeforeDelete(final Object entity) {
        Class<?> domainType = entity.getClass();
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = this.configuration.forManagedDomainType(domainType);

        PersistentEntity<?, ?> persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();

        persistentEntity.doWithProperties(new PersistentPropertyFileDeletionHandler(entity));
    }

    @Override
    protected void onBeforeSave(final Object entity) {
        final Class<?> domainType = entity.getClass();
        final DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = this.configuration.forManagedDomainType(domainType);

        PersistentEntity<?, ?> persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();

        logger.info("One step before saving {}", domainTypeAdministrationConfiguration.getDomainTypeName());

        persistentEntity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> property) {
                Object propertyValue = new DirectFieldAccessFallbackBeanWrapper(entity).getPropertyValue(property.getName());

                logger.info("Property {} has a value of {}", property.getName(), propertyValue);
            }
        });
    }

    private class PersistentPropertyFileDeletionHandler implements SimplePropertyHandler {
        private final Object entity;

        public PersistentPropertyFileDeletionHandler(Object entity) {
            this.entity = entity;
        }

        @Override
        public void doWithPersistentProperty(PersistentProperty<?> property) {
            if (PersistentPropertyType.isOfFileReferenceType(property)) {
                fileResourceStorage.delete(entity, property);
            }
        }
    }
}