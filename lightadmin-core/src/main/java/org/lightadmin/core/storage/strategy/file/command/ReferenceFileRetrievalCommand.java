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

import static org.apache.commons.io.FileUtils.readFileToByteArray;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class ReferenceFileRetrievalCommand extends ReferenceFileCommand {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[]{};

    private final ReferenceFileExistsCommand referenceFileExistsCommand;

    public ReferenceFileRetrievalCommand(FilePathResolver pathResolver) {
        super(pathResolver);
        this.referenceFileExistsCommand = new ReferenceFileExistsCommand(pathResolver);
    }

    public byte[] execute(Object entity, PersistentProperty persistentProperty) throws IOException {
        boolean hasReferencedFile = referenceFileExistsCommand.execute(entity, persistentProperty);
        if (hasReferencedFile) {
            File propertyFileReference = pathResolver.persistentPropertyFileReference(entity, persistentProperty);
            return readFileToByteArray(propertyFileReference);
        }
        return EMPTY_BYTE_ARRAY;
    }
}