package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.domain.field.FieldMetadata;

import java.util.Collection;

public interface FieldMetadataValidator<T extends FieldMetadata> {

    Collection<? extends DomainConfigurationProblem> validateFieldMetadata(T fieldMetadata, Class<?> domainType, DomainConfigurationValidationContext validationContext);

}