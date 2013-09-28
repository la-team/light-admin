package org.lightadmin.core.rest.binary;

import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.data.rest.repository.AttributeMetadata;

import static org.apache.commons.io.FileUtils.deleteQuietly;

public class DeleteFileRestOperation extends AbstractFileRestOperation {

    public DeleteFileRestOperation(GlobalAdministrationConfiguration configuration, WebContext webContext, Object entity) {
        super(configuration, webContext, entity);
    }

    public void perform(AttributeMetadata attrMeta) {
        if (fileStorageModeDisabled()) {
            resetAttrValue(attrMeta);
            repository().save(entity);
            return;
        }

        if (attrMeta.hasAnnotation(FileReference.class)) {
            FileReference fileReference = attrMeta.annotation(FileReference.class);
            DomainTypeAttributeMetadata referenceDomainAttribute = referenceDomainAttribute(fileReference);

            if (isOfStringType(referenceDomainAttribute)) {
                resetAttrValue(attrMeta);
                resetAttrValue(referenceDomainAttribute);
                repository().save(entity);
                deleteQuietly(referencedFile(fileReference));
            }

            return;
        }

        resetAttrValue(attrMeta);
        repository().save(entity);
        deleteQuietly(fileStorageFile(attrMeta));
    }
}