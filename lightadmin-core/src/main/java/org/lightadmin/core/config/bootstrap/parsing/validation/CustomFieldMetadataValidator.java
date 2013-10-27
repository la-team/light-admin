package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;

class CustomFieldMetadataValidator implements FieldMetadataValidator<CustomFieldMetadata> {

    @Override
    public Collection<? extends DomainConfigurationProblem> validateFieldMetadata(CustomFieldMetadata fieldMetadata, Class<?> domainType, DomainConfigurationValidationContext validationContext) {
        if (fieldMetadata.getRenderer() == null) {
            return newArrayList(validationContext.rendererNotDefinedForFieldProblem(fieldMetadata.getName()));
        }
        return emptyList();
    }
}