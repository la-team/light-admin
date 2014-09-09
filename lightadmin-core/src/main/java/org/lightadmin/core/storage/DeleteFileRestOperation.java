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

import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PersistentProperty;

import java.io.File;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

class DeleteFileRestOperation extends AbstractFileRestOperation {

    private static final Logger logger = LoggerFactory.getLogger(DeleteFileRestOperation.class);

    public DeleteFileRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        super(configuration, lightAdminConfiguration, entity);
    }

    public void perform(PersistentProperty persistentProperty) {
        if (isOfBinaryType(persistentProperty)) {
            deletePropertyValue(persistentProperty);
        } else if (hasFileReferenceAnnotation(persistentProperty)) {
            deleteFileReferencedPropertyValue(persistentProperty);
        }
    }

    private void deleteFileReferencedPropertyValue(PersistentProperty persistentProperty) {
        logger.info("Performing delete operation on @FileReference property {}", persistentProperty.getName());

        if (deleteFile(persistentProperty)) {
            deletePropertyValue(persistentProperty);

            removeDomainEntityAttributeDirectory(persistentProperty);
        }

        removeDomainEntityDirectory(persistentProperty);
    }

    private boolean deleteFile(PersistentProperty persistentProperty) {
        if (fieldLevelBaseDirectoryDefined(persistentProperty)) {
            File referencedFile = referencedFile(persistentProperty);

            logger.info("Deleting property-related file {}", referencedFile.getAbsolutePath());
            return deleteQuietly(referencedFile);
        }

        File fileStorageFile = fileStorageFile(persistentProperty);
        logger.info("Deleting property-related file {}", fileStorageFile.getAbsolutePath());
        return deleteQuietly(fileStorageFile);
    }

    private void removeDomainEntityDirectory(PersistentProperty persistentProperty) {
        File domainEntityDirectory = domainEntityDirectory(persistentProperty);

        logger.info("Deleting entity-related directory {}", domainEntityDirectory.getAbsolutePath());

        removeDirectoryIfEmpty(domainEntityDirectory);
    }

    private void removeDomainEntityAttributeDirectory(PersistentProperty persistentProperty) {
        File directoryToRemove = domainEntityAttributeDirectory(persistentProperty);

        logger.info("Deleting property-related directory {}", directoryToRemove.getAbsolutePath());

        removeDirectoryIfEmpty(directoryToRemove);
    }

    private File domainEntityDirectory(PersistentProperty persistentProperty) {
        if (fieldLevelBaseDirectoryDefined(persistentProperty)) {
            return referencedFileDomainEntityDirectory(persistentProperty);
        }
        return fileStorageDomainEntityDirectory();
    }

    private File domainEntityAttributeDirectory(PersistentProperty persistentProperty) {
        if (fieldLevelBaseDirectoryDefined(persistentProperty)) {
            return referencedFileDomainEntityAttributeDirectory(persistentProperty);
        }
        return fileStorageDomainEntityAttributeDirectory(persistentProperty);
    }

    private void removeDirectoryIfEmpty(File domainEntityDirectory) {
        if (isEmpty(domainEntityDirectory.listFiles())) {
            deleteQuietly(domainEntityDirectory);
        }
    }

    private boolean fieldLevelBaseDirectoryDefined(PersistentProperty persistentProperty) {
        return getFile(fileReference(persistentProperty).baseDirectory()).exists();
    }

    private FileReference fileReference(PersistentProperty persistentProperty) {
        return (FileReference) persistentProperty.findAnnotation(FileReference.class);
    }

    private void deletePropertyValue(PersistentProperty persistentProperty) {
        resetAttrValue(persistentProperty);
        repository().save(entity);
    }

    private boolean hasFileReferenceAnnotation(PersistentProperty persistentProperty) {
        return persistentProperty.isAnnotationPresent(FileReference.class);
    }

    private boolean isOfBinaryType(PersistentProperty persistentProperty) {
        return persistentProperty.getType().equals(byte[].class);
    }
}