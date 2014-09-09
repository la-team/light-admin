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
public class LightAdminFileResourceStorage implements FileResourceStorage {

    private final FileManipulationStrategyFactory fileManipulationStrategyFactory;

    public LightAdminFileResourceStorage(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration) {
        this.fileManipulationStrategyFactory = new FileManipulationStrategyFactory(configuration, lightAdminConfiguration);
    }

    @Override
    public void delete(Object instance, PersistentProperty persistentProperty) {
        fileManipulationStrategy(persistentProperty).deleteFile(instance, persistentProperty);
    }

    @Override
    public void save(Object instance, PersistentProperty persistentProperty, Object value) throws IOException {
        fileManipulationStrategy(persistentProperty).saveFile(instance, persistentProperty, value);
    }

    @Override
    public void cleanup(Object instance, PersistentProperty persistentProperty) throws IOException {
        fileManipulationStrategy(persistentProperty).cleanup(instance, persistentProperty);
    }

    @Override
    public boolean fileExists(Object instance, PersistentProperty persistentProperty) throws IOException {
        return fileManipulationStrategy(persistentProperty).fileExists(instance, persistentProperty);
    }

    @Override
    public byte[] load(Object instance, PersistentProperty persistentProperty) throws IOException {
        return fileManipulationStrategy(persistentProperty).loadFile(instance, persistentProperty);
    }

    @Override
    public long copy(Object instance, PersistentProperty persistentProperty, OutputStream outputStream) throws IOException {
        return fileManipulationStrategy(persistentProperty).copyFile(instance, persistentProperty, outputStream);
    }

    private FileManipulationStrategy fileManipulationStrategy(PersistentProperty persistentProperty) {
        return fileManipulationStrategyFactory.createForFileProperty(persistentProperty);
    }
}