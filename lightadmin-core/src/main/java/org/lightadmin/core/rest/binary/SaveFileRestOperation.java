package org.lightadmin.core.rest.binary;

import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.BeanWrapper;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.*;
import static org.lightadmin.core.rest.binary.FileStorageUtils.relativePathToStoreBinaryAttrValue;
import static org.springframework.security.crypto.codec.Base64.decode;
import static org.springframework.security.crypto.codec.Base64.isBase64;
import static org.springframework.util.StringUtils.isEmpty;

public class SaveFileRestOperation extends AbstractFileRestOperation {

    protected SaveFileRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        super(configuration, lightAdminConfiguration, entity);
    }

    public void perform(PersistentProperty attrMeta, Object incomingValueObject) throws IOException {
        if (attrMeta.getType().equals(byte[].class)) {
            performDirectSave(attrMeta, (byte[]) incomingValueObject);
            return;
        }

        if (attrMeta.isAnnotationPresent(FileReference.class)) {
            final byte[] incomingVal = incomingValue(incomingValueObject);

            final FileReference fileReference = (FileReference) attrMeta.findAnnotation(FileReference.class);
            if (getFile(fileReference.baseDirectory()).exists()) {
                performSaveAgainstReferenceField(attrMeta, incomingVal);
            } else {
                performSaveToFileStorage(attrMeta, incomingVal);
            }
        }
    }

    public void performCleanup(PersistentProperty fileReferenceAttrMetadata) throws IOException {
        final JpaRepository repository = domainTypeAdministrationConfiguration.getRepository();
        final FileReference fileReference = (FileReference) fileReferenceAttrMetadata.findAnnotation(FileReference.class);

        if (getFile(fileReference.baseDirectory()).exists()) {
            File file = referencedFile(fileReferenceAttrMetadata);
            byte[] fileData = readFileToByteArray(file);
            deleteQuietly(file);
            perform(fileReferenceAttrMetadata, fileData);

        } else {
            File file = fileStorageFile(fileReferenceAttrMetadata);
            byte[] fileData = readFileToByteArray(file);
            deleteQuietly(file);
            perform(fileReferenceAttrMetadata, fileData);
        }

        repository.save(entity);
    }

    private byte[] incomingValue(Object incomingValueObject) {
        final byte[] incomingValue = incomingValueObject instanceof String
                ? ((String) incomingValueObject).getBytes()
                : (byte[]) incomingValueObject;

        return isBase64(incomingValue) ? decode(incomingValue) : incomingValue;
    }

    private void performSaveToFileStorage(PersistentProperty attrMeta, byte[] incomingVal) throws IOException {
        String relativePath = relativePathToStoreBinaryAttrValue(domainTypeName(), idAttributeValue(), attrMeta);

        File file = getFile(lightAdminConfiguration.getFileStorageDirectory(), relativePath);

        if (isEmpty(incomingVal)) {
            resetAttrValue(attrMeta);
            deleteQuietly(file);
        } else {
            writeByteArrayToFile(file, incomingVal);
            BeanWrapper.create(entity, null).setProperty(attrMeta, relativePath);
        }
    }

    private void performSaveAgainstReferenceField(PersistentProperty attrMeta, byte[] incomingVal) throws IOException {
        final FileReference fileReference = (FileReference) attrMeta.findAnnotation(FileReference.class);

        String relativePath = relativePathToStoreBinaryAttrValue(domainTypeName(), idAttributeValue(), attrMeta);

        File file = getFile(fileReference.baseDirectory(), relativePath);

        if (isEmpty(incomingVal)) {
            resetAttrValue(attrMeta);
            deleteQuietly(file);
        } else {
            writeByteArrayToFile(file, incomingVal);
            BeanWrapper.create(entity, null).setProperty(attrMeta, relativePath);
        }
    }
}