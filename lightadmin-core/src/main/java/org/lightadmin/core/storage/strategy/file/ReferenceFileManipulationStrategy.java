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
package org.lightadmin.core.storage.strategy.file;

import org.lightadmin.core.storage.FileManipulationStrategy;
import org.lightadmin.core.storage.strategy.file.command.*;
import org.springframework.data.mapping.PersistentProperty;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class ReferenceFileManipulationStrategy implements FileManipulationStrategy {

    private final Map<Class<? extends ReferenceFileCommand>, ReferenceFileCommand> commandRegistry;

    public ReferenceFileManipulationStrategy(FilePathResolver filePathResolver) {
        this.commandRegistry = createRegistry(filePathResolver);
    }

    @Override
    public void deleteFile(Object entity, PersistentProperty persistentProperty) {
        getCommand(ReferenceFileDeletionCommand.class).execute(entity, persistentProperty);
    }

    @Override
    public boolean fileExists(Object entity, PersistentProperty persistentProperty) throws IOException {
        return getCommand(ReferenceFileExistsCommand.class).execute(entity, persistentProperty);
    }

    @Override
    public byte[] loadFile(Object entity, PersistentProperty persistentProperty) throws IOException {
        return getCommand(ReferenceFileRetrievalCommand.class).execute(entity, persistentProperty);
    }

    @Override
    public long copyFile(Object entity, PersistentProperty persistentProperty, OutputStream outputStream) throws IOException {
        return getCommand(ReferenceFileCopyCommand.class).execute(entity, persistentProperty, outputStream);
    }

    @Override
    public void saveFile(Object entity, PersistentProperty persistentProperty, Object value) throws IOException {
        getCommand(ReferenceFileSaveCommand.class).execute(entity, persistentProperty, value);
    }

    @Override
    public void cleanup(Object entity, PersistentProperty persistentProperty) throws IOException {
        getCommand(ReferenceFileCleanupCommand.class).execute(entity, persistentProperty);
    }

    @SuppressWarnings("unchecked")
    private <T extends ReferenceFileCommand> T getCommand(Class<T> commandClass) {
        return (T) commandRegistry.get(commandClass);
    }

    private Map<Class<? extends ReferenceFileCommand>, ReferenceFileCommand> createRegistry(FilePathResolver pathResolver) {
        Map<Class<? extends ReferenceFileCommand>, ReferenceFileCommand> commandRegistry = new HashMap<Class<? extends ReferenceFileCommand>, ReferenceFileCommand>();
        commandRegistry.put(ReferenceFileDeletionCommand.class, new ReferenceFileDeletionCommand(pathResolver));
        commandRegistry.put(ReferenceFileExistsCommand.class, new ReferenceFileExistsCommand(pathResolver));
        commandRegistry.put(ReferenceFileRetrievalCommand.class, new ReferenceFileRetrievalCommand(pathResolver));
        commandRegistry.put(ReferenceFileCopyCommand.class, new ReferenceFileCopyCommand(pathResolver));
        commandRegistry.put(ReferenceFileSaveCommand.class, new ReferenceFileSaveCommand(pathResolver));
        commandRegistry.put(ReferenceFileCleanupCommand.class, new ReferenceFileCleanupCommand(pathResolver));
        return commandRegistry;
    }
}