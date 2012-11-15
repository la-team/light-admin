package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.domain.field.TransientFieldMetadata;

class TransientFieldMetadataValidator extends AbstractFieldMetadataValidator<TransientFieldMetadata> {

	@Override
	protected String getFieldMetadataPropertyPath( final TransientFieldMetadata fieldMetadata ) {
		return fieldMetadata.getProperty();
	}
}