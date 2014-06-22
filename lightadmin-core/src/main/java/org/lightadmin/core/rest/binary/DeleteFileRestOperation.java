package org.lightadmin.core.rest.binary;

import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.mapping.PersistentProperty;

import java.io.File;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class DeleteFileRestOperation extends AbstractFileRestOperation {

    public DeleteFileRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        super(configuration, lightAdminConfiguration, entity);
    }

    public void perform(PersistentProperty attrMeta) {
        if (attrMeta.getType().equals(byte[].class)) {
            resetAttrValue(attrMeta);
            repository().save(entity);
            return;
        }

        if (attrMeta.isAnnotationPresent(FileReference.class)) {
            if (deleteFile(attrMeta)) {
                resetAttrValue(attrMeta);
                repository().save(entity);

                removeDomainEntityAttributeDirectory(attrMeta);
            }

            removeDomainEntityDirectory(attrMeta);
        }
    }

    private boolean deleteFile(PersistentProperty attrMeta) {
        if (fieldLevelBaseDirectoryDefined(attrMeta)) {
            return deleteQuietly(referencedFile(attrMeta));
        }
        return deleteQuietly(fileStorageFile(attrMeta));
    }

    private void removeDomainEntityDirectory(PersistentProperty attrMeta) {
        if (fieldLevelBaseDirectoryDefined(attrMeta)) {
            removeDirectoryIfEmpty(referencedFileDomainEntityDirectory(attrMeta));
        } else {
            removeDirectoryIfEmpty(fileStorageDomainEntityDirectory());
        }
    }

    private void removeDomainEntityAttributeDirectory(PersistentProperty attrMeta) {
        if (fieldLevelBaseDirectoryDefined(attrMeta)) {
            removeDirectoryIfEmpty(referencedFileDomainEntityAttributeDirectory(attrMeta));
        } else {
            removeDirectoryIfEmpty(fileStorageDomainEntityAttributeDirectory(attrMeta));
        }
    }

    private void removeDirectoryIfEmpty(File domainEntityDirectory) {
        if (isEmpty(domainEntityDirectory.listFiles())) {
            deleteQuietly(domainEntityDirectory);
        }
    }

    private boolean fieldLevelBaseDirectoryDefined(PersistentProperty attrMeta) {
        return getFile(fileReference(attrMeta).baseDirectory()).exists();
    }

    private FileReference fileReference(PersistentProperty attrMeta) {
        return (FileReference) attrMeta.findAnnotation(FileReference.class);
    }
}