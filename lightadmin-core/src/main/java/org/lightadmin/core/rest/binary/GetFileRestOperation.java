package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.data.rest.repository.AttributeMetadata;

import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class GetFileRestOperation extends AbstractFileRestOperation {

    protected GetFileRestOperation(GlobalAdministrationConfiguration configuration, WebContext webContext, Object entity) {
        super(configuration, webContext, entity);
    }

    public byte[] perform(AttributeMetadata attrMeta) throws IOException {
        if (fileStorageModeDisabled()) {
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
}