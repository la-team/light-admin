package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.domain.field.CustomFieldMetadata;

class CustomFieldMetadataValidator implements FieldMetadataValidator<CustomFieldMetadata> {

	@Override
	public boolean isValidFieldMetadata( final CustomFieldMetadata fieldMetadata, final Class<?> domainType ) {
		return fieldMetadata.getRenderer() != null;
	}
}