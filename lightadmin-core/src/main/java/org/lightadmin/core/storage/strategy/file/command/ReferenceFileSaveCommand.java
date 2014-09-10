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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ReferenceFileSaveCommand.class);

    public ReferenceFileSaveCommand(FilePathResolver pathResolver) {
        super(pathResolver);
    }

    public void execute(Object entity, PersistentProperty persistentProperty, Object incomingValueObject) throws IOException {
        logger.info("Starting save command execution for {}", persistentProperty.getName());

        byte[] incomingVal = incomingValue(incomingValueObject);

        logger.info("Received data for persisting {}: {}", persistentProperty.getName(), incomingVal);

        String relativePath = pathResolver.persistentPropertyFileRelativePath(entity, persistentProperty);

        logger.info("Property {} relative file path {}", persistentProperty.getName(), relativePath);

        File file = pathResolver.persistentPropertyFileReference(entity, persistentProperty);

        if (ArrayUtils.isEmpty(incomingVal)) {
            logger.info("Incoming value is empty");

            resetPropertyValue(entity, persistentProperty);

            logger.info("Resetted property value to {}", getPropertyValue(entity, persistentProperty));

            boolean fileDeleted = deleteQuietly(file);
            if (fileDeleted) {
                logger.info("File {} has been deleted", file);
            }

            return;
        }

        logger.info("Writing binary data to file {}: {}", file, incomingVal);

        writeByteArrayToFile(file, incomingVal);

        logger.info("Settign property {} new value {}", persistentProperty.getName(), relativePath);

        setPropertyValue(entity, persistentProperty, relativePath);

        logger.info("Current property {} value is {}", persistentProperty.getName(), getPropertyValue(entity, persistentProperty));
    }

    private byte[] incomingValue(Object incomingValueObject) {
        byte[] incomingValue = incomingValueObject instanceof String
                ? ((String) incomingValueObject).getBytes()
                : (byte[]) incomingValueObject;

        return isBase64(incomingValue) ? decode(incomingValue) : incomingValue;
    }
}