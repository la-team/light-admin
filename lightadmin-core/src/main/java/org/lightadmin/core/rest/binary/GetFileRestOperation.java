package org.lightadmin.core.rest.binary;

import org.apache.commons.lang.ArrayUtils;
import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.springframework.data.rest.repository.AttributeMetadata;
import org.springframework.util.FileCopyUtils;

import java.io.*;

import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class GetFileRestOperation extends AbstractFileRestOperation {

    protected GetFileRestOperation(GlobalAdministrationConfiguration configuration, WebContext webContext, Object entity) {
        super(configuration, webContext, entity);
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

    public void performCopy(AttributeMetadata attrMeta, OutputStream outputStream) throws IOException {
        if (attrMeta.type().equals(byte[].class)) {
            byte[] fileData = (byte[]) attrMeta.get(entity);
            if (ArrayUtils.isNotEmpty(fileData)) {
                FileCopyUtils.copy(fileData, outputStream);
            }
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            FileReference fileReference = attrMeta.annotation(FileReference.class);

            if (getFile(fileReference.baseDirectory()).exists()) {
                copyToOutputStream(referencedFile(attrMeta), outputStream);
            } else {
                copyToOutputStream(fileStorageFile(attrMeta), outputStream);
            }
        }
    }

    private void copyToOutputStream(File file, OutputStream outputStream) throws IOException {
        FileCopyUtils.copy(new BufferedInputStream(new FileInputStream(file)), new BufferedOutputStream(outputStream));
    }
}