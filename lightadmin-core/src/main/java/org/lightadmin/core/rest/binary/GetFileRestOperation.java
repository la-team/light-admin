package org.lightadmin.core.rest.binary;

import org.apache.commons.lang3.ArrayUtils;
import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.BeanWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.commons.io.FileUtils.*;
import static org.springframework.util.FileCopyUtils.copy;

public class GetFileRestOperation extends AbstractFileRestOperation {

    protected GetFileRestOperation(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, Object entity) {
        super(configuration, lightAdminConfiguration, entity);
    }

    public byte[] perform(PersistentProperty persistentProperty) throws IOException {
        if (persistentProperty.getType().equals(byte[].class)) {
            return (byte[]) BeanWrapper.create(entity, null).getProperty(persistentProperty);
        }

        if (persistentProperty.isAnnotationPresent(FileReference.class)) {
            FileReference fileReference = (FileReference) persistentProperty.findAnnotation(FileReference.class);

            if (getFile(fileReference.baseDirectory()).exists()) {
                return readFileToByteArray(referencedFile(persistentProperty));
            } else {
                return readFileToByteArray(fileStorageFile(persistentProperty));
            }
        }
        return new byte[]{};
    }

    public long performCopy(PersistentProperty persistentProperty, OutputStream outputStream) throws IOException {
        if (persistentProperty.getType().equals(byte[].class)) {
            byte[] fileData = (byte[]) BeanWrapper.create(entity, null).getProperty(persistentProperty);
            if (ArrayUtils.isNotEmpty(fileData)) {
                copy(fileData, outputStream);
                return fileData.length;
            }
        }

        if (persistentProperty.isAnnotationPresent(FileReference.class)) {
            FileReference fileReference = (FileReference) persistentProperty.findAnnotation(FileReference.class);

            if (getFile(fileReference.baseDirectory()).exists()) {
                return copyToOutputStream(referencedFile(persistentProperty), outputStream);
            }

            return copyToOutputStream(fileStorageFile(persistentProperty), outputStream);
        }

        return 0l;
    }

    private long copyToOutputStream(File file, OutputStream outputStream) throws IOException {
        copy(new FileInputStream(file), outputStream);

        return sizeOf(file);
    }
}