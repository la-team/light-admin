package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.springframework.data.rest.repository.AttributeMetadata;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.getFile;

public class DeleteFileRestOperation extends AbstractFileRestOperation {

    public DeleteFileRestOperation(GlobalAdministrationConfiguration configuration, WebContext webContext, Object entity) {
        super(configuration, webContext, entity);
    }

    public void perform(AttributeMetadata attrMeta) {
        if (attrMeta.type().equals(byte[].class)) {
            resetAttrValue(attrMeta);
            repository().save(entity);
            return;
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            final FileReference fileReference = attrMeta.annotation(FileReference.class);

            if (deleteFile(attrMeta, fileReference)) {
                resetAttrValue(attrMeta);
                repository().save(entity);
            }
        }
    }

    public void performDirectoryCleanup() {
        // TODO
    }

    private boolean deleteFile(AttributeMetadata attrMeta, FileReference fileReference) {
        if (getFile(fileReference.baseDirectory()).exists()) {
            return deleteQuietly(referencedFile(attrMeta));
        }
        return deleteQuietly(fileStorageFile(attrMeta));
    }
}