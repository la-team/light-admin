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
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.mapping.PersistentProperty;

import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class FileSystemStorage implements FileResourceStorage {

    private final OperationCreator operationCreator;

    public FileSystemStorage(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
        this.operationCreator = new OperationCreator(configuration, lightAdminConfiguration);
    }

    @Override
    public void delete(Object instance, PersistentProperty persistentProperty) {
        this.operationCreator.deleteOperation(instance).perform(persistentProperty);
    }

    @Override
    public void save(Object instance, PersistentProperty persistentProperty, Object value) throws IOException {
        operationCreator.saveOperation(instance).perform(persistentProperty, value);
    }

    @Override
    public void save(Object instance, PersistentProperty persistentProperty) throws IOException {
        operationCreator.saveOperation(instance).performCleanup(persistentProperty);
    }

    @Override
    public boolean fileExists(Object instance, PersistentProperty persistentProperty) throws IOException {
        return operationCreator.fileExistsOperation(instance).perform(persistentProperty);
    }

    @Override
    public byte[] load(Object instance, PersistentProperty persistentProperty) throws IOException {
        return operationCreator.getOperation(instance).perform(persistentProperty);
    }

    @Override
    public long copy(Object instance, PersistentProperty persistentProperty, OutputStream outputStream) throws IOException {
        return operationCreator.getOperation(instance).performCopy(persistentProperty, outputStream);
    }

    static class OperationCreator {

        private GlobalAdministrationConfiguration configuration;
        private LightAdminConfiguration lightAdminConfiguration;

        private OperationCreator(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
            this.configuration = configuration;
            this.lightAdminConfiguration = lightAdminConfiguration;
        }

        public SaveFileRestOperation saveOperation(Object entity) {
            return new SaveFileRestOperation(configuration, lightAdminConfiguration, entity);
        }

        public DeleteFileRestOperation deleteOperation(Object entity) {
            return new DeleteFileRestOperation(configuration, lightAdminConfiguration, entity);
        }

        public GetFileRestOperation getOperation(Object entity) {
            return new GetFileRestOperation(configuration, lightAdminConfiguration, entity);
        }

        public FileExistsRestOperation fileExistsOperation(Object entity) {
            return new FileExistsRestOperation(configuration, lightAdminConfiguration, entity);
        }
    }
}