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
package org.lightadmin.core.storage;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.storage.strategy.binary.BinaryFileManipulationStrategy;
import org.lightadmin.core.storage.strategy.file.FilePathResolver;
import org.lightadmin.core.storage.strategy.file.ReferenceFileManipulationStrategy;
import org.lightadmin.core.storage.strategy.file.ReferenceFilePathResolver;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.isOfBinaryFileType;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.isOfFileReferenceType;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class FileManipulationStrategyFactory {

    private static final NoopFileManipulationStrategy NOOP_FILE_MANIPULATION_STRATEGY = new NoopFileManipulationStrategy();

    private final GlobalAdministrationConfiguration configuration;
    private final LightAdminConfiguration lightAdminConfiguration;

    public FileManipulationStrategyFactory(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
        this.lightAdminConfiguration = lightAdminConfiguration;
        this.configuration = configuration;
    }

    public FileManipulationStrategy createForFileProperty(PersistentProperty persistentProperty) {
        PersistentEntity persistentEntity = persistentProperty.getOwner();

        if (isOfBinaryFileType(persistentProperty)) {
            return createBinaryFileManipulationStrategy();
        }

        if (isOfFileReferenceType(persistentProperty)) {
            return createReferenceFileManipulationStrategy(persistentEntity);
        }

        return NOOP_FILE_MANIPULATION_STRATEGY;
    }

    private FileManipulationStrategy createReferenceFileManipulationStrategy(PersistentEntity persistentEntity) {
        File fileStorageDirectory = lightAdminConfiguration.getFileStorageDirectory();
        DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = configuration.forManagedDomainType(ClassUtils.getUserClass(persistentEntity.getType()));
        String domainTypeName = domainTypeAdministrationConfiguration.getDomainTypeName();

        FilePathResolver pathResolver = new ReferenceFilePathResolver(fileStorageDirectory, persistentEntity, domainTypeName);

        return new ReferenceFileManipulationStrategy(pathResolver);
    }

    private BinaryFileManipulationStrategy createBinaryFileManipulationStrategy() {
        return new BinaryFileManipulationStrategy();
    }

    private static class NoopFileManipulationStrategy implements FileManipulationStrategy {
        @Override
        public void deleteFile(Object entity, PersistentProperty persistentProperty) {
        }

        @Override
        public boolean fileExists(Object entity, PersistentProperty persistentProperty) throws IOException {
            return false;
        }

        @Override
        public byte[] loadFile(Object entity, PersistentProperty persistentProperty) throws IOException {
            return new byte[0];
        }

        @Override
        public long copyFile(Object entity, PersistentProperty persistentProperty, OutputStream outputStream) throws IOException {
            return 0l;
        }

        @Override
        public void saveFile(Object entity, PersistentProperty persistentProperty, Object value) throws IOException {
        }

        @Override
        public void cleanup(Object entity, PersistentProperty persistentProperty) throws IOException {
        }
    }
}