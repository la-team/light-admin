package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.data.rest.repository.AttributeMetadata;
import org.springframework.util.FileCopyUtils;

import java.io.*;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class GetFileRestOperation extends AbstractFileRestOperation {

    protected GetFileRestOperation(GlobalAdministrationConfiguration configuration, WebContext webContext, Object entity) {
        super(configuration, webContext, entity);
    }

    public byte[] perform(AttributeMetadata attrMeta) throws IOException {
        if (fileStorageModeDisabled() || fileStorageDisabledOnFieldLevel(attrMeta)) {
            return (byte[]) attrMeta.get(entity);
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            FileReference fileReference = attrMeta.annotation(FileReference.class);
            DomainTypeAttributeMetadata referenceDomainAttribute = referenceDomainAttribute(fileReference);

            if (isOfStringType(referenceDomainAttribute)) {
                return readFileToByteArray(referencedFile(fileReference));
            }

            return new byte[0];
        }

        return readFileToByteArray(fileStorageFile(attrMeta));
    }

    public void performCopy(AttributeMetadata attrMeta, OutputStream outputStream) throws IOException {
        if (fileStorageModeDisabled() || fileStorageDisabledOnFieldLevel(attrMeta)) {
            FileCopyUtils.copy((byte[]) attrMeta.get(entity), outputStream);
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            FileReference fileReference = attrMeta.annotation(FileReference.class);
            DomainTypeAttributeMetadata referenceDomainAttribute = referenceDomainAttribute(fileReference);

            if (isOfStringType(referenceDomainAttribute)) {
                copyToOutputStream(referencedFile(fileReference), outputStream);
            }
            return;
        }

        copyToOutputStream(fileStorageFile(attrMeta), outputStream);
    }

    private void copyToOutputStream(File file, OutputStream outputStream) throws IOException {
        FileCopyUtils.copy(new BufferedInputStream(new FileInputStream(file)), new BufferedOutputStream(outputStream));
    }
}