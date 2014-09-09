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
package org.lightadmin.core.storage.strategy.file.command;

import org.lightadmin.core.storage.strategy.file.FilePathResolver;
import org.springframework.data.mapping.PersistentProperty;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class ReferenceFileCleanupCommand extends ReferenceFileCommand {

    private final ReferenceFileSaveCommand referenceFileSaveCommand;

    public ReferenceFileCleanupCommand(FilePathResolver pathResolver) {
        super(pathResolver);

        this.referenceFileSaveCommand = new ReferenceFileSaveCommand(pathResolver);
    }

    public void execute(Object entity, PersistentProperty persistentProperty) throws IOException {
        File file = this.pathResolver.persistentPropertyFileReference(entity, persistentProperty);

        byte[] fileData = readFileToByteArray(file);

        if (deleteQuietly(file)) {
            this.referenceFileSaveCommand.execute(entity, persistentProperty, fileData);
        }
    }
}