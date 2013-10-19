package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.data.rest.repository.AttributeMetadata;

import java.io.File;

import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.isOfFileReferenceType;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.isSupportedAttributeType;

class PersistentFieldMetadataValidator implements FieldMetadataValidator<PersistentFieldMetadata> {

    private final DomainTypeEntityMetadataResolver entityMetadataResolver;
    private final WebContext webContext;

    PersistentFieldMetadataValidator(final DomainTypeEntityMetadataResolver entityMetadataResolver, WebContext webContext) {
        this.entityMetadataResolver = entityMetadataResolver;
        this.webContext = webContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isValidFieldMetadata(final PersistentFieldMetadata fieldMetadata, final Class<?> domainType) {
        final DomainTypeEntityMetadata domainTypeEntityMetadata = entityMetadataResolver.resolveEntityMetadata(domainType);

        final DomainTypeAttributeMetadata domainTypeAttributeMetadata = domainTypeEntityMetadata.getAttribute(fieldMetadata.getField());

        if (domainTypeAttributeMetadata == null) {
            return false;
        }

        if (!isSupportedAttributeType(domainTypeAttributeMetadata.getAttributeType())) {
            return false;
        }

        AttributeMetadata attributeMetadata = domainTypeAttributeMetadata.getAttributeMetadata();

        if (isNotFileReferenceField(attributeMetadata)) {
            return true;
        }

        final FileReference fileReference = attributeMetadata.annotation(FileReference.class);

        if (isEmpty(fileReference.baseDirectory())) {
            if (webContext.getFileStorageDirectory() != null) {
                return true;
            }
            return false;
        }

        File directory = getFile(fileReference.baseDirectory());
        if (directory.exists() && directory.isDirectory()) {
            return true;
        }

        return false;

    }

    private boolean isNotFileReferenceField(AttributeMetadata attributeMetadata) {
        return !isOfFileReferenceType(attributeMetadata);
    }
}