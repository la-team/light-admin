package org.lightadmin.core.rest.binary;

import org.apache.commons.lang3.ArrayUtils;
import org.lightadmin.api.config.annotation.FileReference;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.data.rest.repository.AttributeMetadata;

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

    public byte[] perform(AttributeMetadata attrMeta) throws IOException {
        if (attrMeta.type().equals(byte[].class)) {
            return (byte[]) attrMeta.get(entity);
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            FileReference fileReference = attrMeta.annotation(FileReference.class);

            if (getFile(fileReference.baseDirectory()).exists()) {
                return readFileToByteArray(referencedFile(attrMeta));
            } else {
                return readFileToByteArray(fileStorageFile(attrMeta));
            }
        }
        return new byte[]{};
    }

    public long performCopy(AttributeMetadata attrMeta, OutputStream outputStream) throws IOException {
        if (attrMeta.type().equals(byte[].class)) {
            byte[] fileData = (byte[]) attrMeta.get(entity);
            if (ArrayUtils.isNotEmpty(fileData)) {
                copy(fileData, outputStream);
                return fileData.length;
            }
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            FileReference fileReference = attrMeta.annotation(FileReference.class);

            if (getFile(fileReference.baseDirectory()).exists()) {
                return copyToOutputStream(referencedFile(attrMeta), outputStream);
            }

            return copyToOutputStream(fileStorageFile(attrMeta), outputStream);
        }

        return 0l;
    }

    private long copyToOutputStream(File file, OutputStream outputStream) throws IOException {
        copy(new FileInputStream(file), outputStream);

        return sizeOf(file);
    }
}