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

import org.apache.commons.lang3.ArrayUtils;
import org.lightadmin.core.storage.strategy.file.FilePathResolver;
import org.springframework.data.mapping.PersistentProperty;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.springframework.security.crypto.codec.Base64.decode;
import static org.springframework.security.crypto.codec.Base64.isBase64;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class ReferenceFileSaveCommand extends ReferenceFileCommand {

    public ReferenceFileSaveCommand(FilePathResolver pathResolver) {
        super(pathResolver);
    }

    public void execute(Object entity, PersistentProperty persistentProperty, Object incomingValueObject) throws IOException {
        byte[] incomingVal = incomingValue(incomingValueObject);

        String relativePath = pathResolver.persistentPropertyFileRelativePath(entity, persistentProperty);

        File file = pathResolver.persistentPropertyFileReference(entity, persistentProperty);

        if (ArrayUtils.isEmpty(incomingVal)) {
            resetPropertyValue(entity, persistentProperty);
            deleteQuietly(file);
            return;
        }

        writeByteArrayToFile(file, incomingVal);
        setPropertyValue(entity, persistentProperty, relativePath);
    }

    private byte[] incomingValue(Object incomingValueObject) {
        byte[] incomingValue = incomingValueObject instanceof String
                ? ((String) incomingValueObject).getBytes()
                : (byte[]) incomingValueObject;

        return isBase64(incomingValue) ? decode(incomingValue) : incomingValue;
    }
}