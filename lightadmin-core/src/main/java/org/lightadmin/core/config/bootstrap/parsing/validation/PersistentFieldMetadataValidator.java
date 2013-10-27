package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.annotation.FileReference;
import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.data.rest.repository.AttributeMetadata;

import java.io.File;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.isOfFileReferenceType;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.isSupportedAttributeType;

class PersistentFieldMetadataValidator implements FieldMetadataValidator<PersistentFieldMetadata> {

    PersistentFieldMetadataValidator() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends DomainConfigurationProblem> validateFieldMetadata(PersistentFieldMetadata fieldMetadata, Class<?> domainType, DomainConfigurationValidationContext validationContext) {
        final DomainTypeEntityMetadataResolver entityMetadataResolver = validationContext.getEntityMetadataResolver();
        final LightAdminConfiguration lightAdminConfiguration = validationContext.getLightAdminConfiguration();

        final DomainTypeEntityMetadata domainTypeEntityMetadata = entityMetadataResolver.resolveEntityMetadata(domainType);

        final DomainTypeAttributeMetadata domainTypeAttributeMetadata = domainTypeEntityMetadata.getAttribute(fieldMetadata.getField());

        if (domainTypeAttributeMetadata == null) {
            return newArrayList(validationContext.notPersistableFieldProblem(fieldMetadata.getName()));
        }

        if (!isSupportedAttributeType(domainTypeAttributeMetadata.getAttributeType())) {
            return newArrayList(validationContext.notSupportedTypeFieldProblem(fieldMetadata.getName()));
        }

        AttributeMetadata attributeMetadata = domainTypeAttributeMetadata.getAttributeMetadata();

        if (isNotFileReferenceField(attributeMetadata)) {
            return emptyList();
        }

        final FileReference fileReference = attributeMetadata.annotation(FileReference.class);

        if (isEmpty(fileReference.baseDirectory())) {
            if (lightAdminConfiguration.getFileStorageDirectory() != null) {
                return emptyList();
            }
            return newArrayList(validationContext.missingBaseDirectoryInFileReferenceProblem(fieldMetadata.getName()));
        }

        final File directory = getFile(fileReference.baseDirectory());
        if (directory.exists() && directory.isDirectory()) {
            return emptyList();
        }

        return newArrayList(validationContext.missingBaseDirectoryInFileReferenceProblem(fieldMetadata.getName()));

    }

    private boolean isNotFileReferenceField(AttributeMetadata attributeMetadata) {
        return !isOfFileReferenceType(attributeMetadata);
    }
}