package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.rest.repository.AttributeMetadata;

import java.io.File;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang.ArrayUtils.isEmpty;

public class DeleteFileRestOperation extends AbstractFileRestOperation {

    public DeleteFileRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        super(configuration, lightAdminConfiguration, entity);
    }

    public void perform(AttributeMetadata attrMeta) {
        if (attrMeta.type().equals(byte[].class)) {
            resetAttrValue(attrMeta);
            repository().save(entity);
            return;
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            if (deleteFile(attrMeta)) {
                resetAttrValue(attrMeta);
                repository().save(entity);

                removeDomainEntityAttributeDirectory(attrMeta);
            }

            removeDomainEntityDirectory(attrMeta);
        }
    }

    private boolean deleteFile(AttributeMetadata attrMeta) {
        if (fieldLevelBaseDirectoryDefined(attrMeta)) {
            return deleteQuietly(referencedFile(attrMeta));
        }
        return deleteQuietly(fileStorageFile(attrMeta));
    }

    private void removeDomainEntityDirectory(AttributeMetadata attrMeta) {
        if (fieldLevelBaseDirectoryDefined(attrMeta)) {
            removeDirectoryIfEmpty(referencedFileDomainEntityDirectory(attrMeta));
        } else {
            removeDirectoryIfEmpty(fileStorageDomainEntityDirectory());
        }
    }

    private void removeDomainEntityAttributeDirectory(AttributeMetadata attrMeta) {
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

    private boolean fieldLevelBaseDirectoryDefined(AttributeMetadata attrMeta) {
        return getFile(fileReference(attrMeta).baseDirectory()).exists();
    }

    private FileReference fileReference(AttributeMetadata attrMeta) {
        return attrMeta.annotation(FileReference.class);
    }
}