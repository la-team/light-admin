package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.springframework.data.rest.repository.AttributeMetadata;
import org.springframework.security.crypto.codec.Base64;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import static org.lightadmin.core.rest.binary.FileStorageUtils.relativePathToStoreBinaryAttrValue;

public class SaveFileRestOperation extends AbstractFileRestOperation {

    protected SaveFileRestOperation(GlobalAdministrationConfiguration configuration, WebContext webContext, Object entity) {
        super(configuration, webContext, entity);
    }

    public void perform(AttributeMetadata attrMeta, Object incomingValueObject) throws IOException {
        if (attrMeta.type().equals(byte[].class)) {
            performDirectSave(attrMeta, (byte[]) incomingValueObject);
            return;
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            byte[] incomingVal = Base64.decode(((String) incomingValueObject).getBytes());
            final FileReference fileReference = attrMeta.annotation(FileReference.class);
            if (getFile(fileReference.baseDirectory()).exists()) {
                performSaveAgainstReferenceField(attrMeta, incomingVal);
            } else {
                performSaveToFileStorage(attrMeta, incomingVal);
            }
        }
    }

    private void performSaveToFileStorage(AttributeMetadata attrMeta, byte[] incomingVal) throws IOException {
        final File file = fileStorageFile(attrMeta);
        if (isEmpty(incomingVal)) {
            resetAttrValue(attrMeta);
            deleteQuietly(file);
        } else {
            writeByteArrayToFile(file, incomingVal);
            attrMeta.set(relativePathToStoreBinaryAttrValue(domainTypeName(), idAttributeValue(), attrMeta), entity);
        }
    }

    private void performSaveAgainstReferenceField(AttributeMetadata attrMeta, byte[] incomingVal) throws IOException {
        final FileReference fileReference = attrMeta.annotation(FileReference.class);

        String relativePath = relativePathToStoreBinaryAttrValue(domainTypeName(), idAttributeValue(), attrMeta);

        File file = getFile(fileReference.baseDirectory(), relativePath);

        if (isEmpty(incomingVal)) {
            resetAttrValue(attrMeta);
            deleteQuietly(file);
        } else {
            writeByteArrayToFile(file, incomingVal);
            attrMeta.set(relativePath, entity);
        }
    }
}