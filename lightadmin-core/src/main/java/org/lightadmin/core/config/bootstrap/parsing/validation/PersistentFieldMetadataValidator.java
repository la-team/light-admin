package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;

//TODO: We need to have an additional validation rule for Persistent field paths: all nested properties must be persistent
class PersistentFieldMetadataValidator extends AbstractFieldMetadataValidator<PersistentFieldMetadata> {

	@Override
	protected String getFieldMetadataPropertyPath( final PersistentFieldMetadata fieldMetadata ) {
		return fieldMetadata.getField();
	}
}