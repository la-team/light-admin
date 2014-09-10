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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PersistentProperty;

import java.io.File;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.sizeOfDirectory;

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */
public class ReferenceFileDeletionCommand extends ReferenceFileCommand {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceFileDeletionCommand.class);

    private final ReferenceFileExistsCommand referenceFileExistsCommand;

    public ReferenceFileDeletionCommand(FilePathResolver pathResolver) {
        super(pathResolver);

        this.referenceFileExistsCommand = new ReferenceFileExistsCommand(pathResolver);
    }

    public void execute(Object entity, PersistentProperty persistentProperty) {
        logger.info("Performing delete operation on @FileReference property {}", persistentProperty.getName());

        if (hasNoReferencedFile(entity, persistentProperty)) {
            return;
        }

        boolean fileDeleted = deleteFile(entity, persistentProperty);
        if (fileDeleted) {
            resetPropertyValue(entity, persistentProperty);

            deletePropertyFileDirectory(entity, persistentProperty);
        }
    }

    private boolean hasNoReferencedFile(Object entity, PersistentProperty persistentProperty) {
        return !referenceFileExistsCommand.execute(entity, persistentProperty);
    }

    private boolean deleteFile(Object entity, PersistentProperty persistentProperty) {
        File referencedFile = pathResolver.persistentPropertyFileReference(entity, persistentProperty);

        logger.info("Deleting property-related file {}", referencedFile.getAbsolutePath());

        return deleteQuietly(referencedFile);
    }

    private void deletePropertyFileDirectory(Object entity, PersistentProperty persistentProperty) {
        File directoryToRemove = pathResolver.persistentPropertyFileDirectory(entity, persistentProperty);

        logger.info("Deleting property-related directory {}", directoryToRemove.getAbsolutePath());

        removeDirectoryIfEmpty(directoryToRemove);
    }

    private boolean removeDirectoryIfEmpty(File domainEntityDirectory) {
        if (!domainEntityDirectory.exists()) {
            return false;
        }

        if (sizeOfDirectory(domainEntityDirectory) == 0) {
            return false;
        }

        return deleteQuietly(domainEntityDirectory);
    }
}