package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@SuppressWarnings("unchecked")
class DomainTypeFieldMetadataValidator implements FieldMetadataValidator<FieldMetadata> {

    private final Map<Class<? extends FieldMetadata>, FieldMetadataValidator<? extends FieldMetadata>> fieldMetadataValidators = newHashMap();

    public DomainTypeFieldMetadataValidator() {
        fieldMetadataValidators.put(PersistentFieldMetadata.class, new PersistentFieldMetadataValidator());
        fieldMetadataValidators.put(TransientFieldMetadata.class, new TransientFieldMetadataValidator());
        fieldMetadataValidators.put(CustomFieldMetadata.class, new CustomFieldMetadataValidator());
    }

    @Override
    public Collection<? extends DomainConfigurationProblem> validateFieldMetadata(FieldMetadata fieldMetadata, Class<?> domainType, DomainConfigurationValidationContext validationContext) {
        final FieldMetadataValidator fieldMetadataValidator = fieldMetadataValidators.get(fieldMetadata.getClass());

        return fieldMetadataValidator.validateFieldMetadata(fieldMetadata, domainType, validationContext);
    }
}