package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.data.rest.repository.AttributeMetadata;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import static org.lightadmin.core.rest.binary.FileStorageUtils.relativePathToStoreBinaryAttrValue;

public class SaveFileRestOperation extends AbstractFileRestOperation {

    protected SaveFileRestOperation(GlobalAdministrationConfiguration configuration, WebContext webContext, Object entity) {
        super(configuration, webContext, entity);
    }

    public void perform(AttributeMetadata attrMeta, byte[] incomingVal) throws IOException {
        if (fileStorageModeDisabled() || fileStorageDisabledOnFieldLevel(attrMeta)) {
            performDirectSave(attrMeta, incomingVal);
            return;
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            performSaveAgainstReferenceField(attrMeta, incomingVal);
            return;
        }

        performSaveToFileStorage(attrMeta, incomingVal);
    }

    private void performSaveToFileStorage(AttributeMetadata attrMeta, byte[] incomingVal) throws IOException {
        final File file = fileStorageFile(attrMeta);

        if (isEmpty(incomingVal)) {
            resetAttrValue(attrMeta);
            deleteQuietly(file);
            return;
        }

        writeByteArrayToFile(file, incomingVal);
        performDirectSave(attrMeta, binaryFilePath(file));
    }

    private void performSaveAgainstReferenceField(AttributeMetadata attrMeta, byte[] incomingVal) throws IOException {
        final FileReference fileReference = attrMeta.annotation(FileReference.class);
        final DomainTypeAttributeMetadata referenceDomainAttribute = referenceDomainAttribute(fileReference);

        if (isOfStringType(referenceDomainAttribute)) {
            String relativePath = relativePathToStoreBinaryAttrValue(domainTypeName(), idAttributeValue(), attrMeta);

            File file = getFile(fileReference.baseDirectory(), relativePath);

            if (isEmpty(incomingVal)) {
                resetAttrValue(referenceDomainAttribute);
                resetAttrValue(attrMeta);
                deleteQuietly(file);
                return;
            }

            writeByteArrayToFile(file, incomingVal);
            referenceDomainAttribute.setValue(relativePath, entity);
            performDirectSave(attrMeta, binaryFilePath(file));
        }
    }

    private byte[] binaryFilePath(File file) throws UnsupportedEncodingException {
        return file.getPath().getBytes("UTF-8");
    }
}