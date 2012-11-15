package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.domain.field.FieldMetadata;

public interface FieldMetadataValidator<T extends FieldMetadata> {

	boolean isValidFieldMetadata( T fieldMetadata, Class<?> domainType );

}