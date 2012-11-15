package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;

class PersistentFieldMetadataValidator extends AbstractFieldMetadataValidator<PersistentFieldMetadata> {

	@Override
	protected String getFieldMetadataPropertyPath( final PersistentFieldMetadata fieldMetadata ) {
		return fieldMetadata.getField();
	}
}