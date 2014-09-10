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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.springframework.util.FileCopyUtils.copy;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class ReferenceFileCopyCommand extends ReferenceFileCommand {

    private static final long ZERO_LENGTH = 0l;

    private final ReferenceFileExistsCommand referenceFileExistsCommand;

    public ReferenceFileCopyCommand(FilePathResolver pathResolver) {
        super(pathResolver);

        this.referenceFileExistsCommand = new ReferenceFileExistsCommand(pathResolver);
    }

    public long execute(Object entity, PersistentProperty persistentProperty, OutputStream outputStream) throws IOException {
        boolean hasReferencedFile = this.referenceFileExistsCommand.execute(entity, persistentProperty);

        if (hasReferencedFile) {
            File propertyFileReference = this.pathResolver.persistentPropertyFileReference(entity, persistentProperty);

            return copyToOutputStream(propertyFileReference, outputStream);
        }

        closeQuietly(outputStream);

        return ZERO_LENGTH;
    }

    private long copyToOutputStream(File file, OutputStream outputStream) throws IOException {
        return copy(new FileInputStream(file), outputStream);
    }
}