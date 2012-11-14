package org.lightadmin.core.config.domain.field;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;

public abstract class FieldMetadataFactory {

	public static FieldMetadata persistentField( String name, String persistentField ) {
		return new PersistentFieldMetadata( name, persistentField );
	}

	public static FieldMetadata transientField( final String name, final String transientProperty ) {
		return new TransientFieldMetadata( name, transientProperty );
	}

	public static FieldMetadata customField( final String name, final FieldValueRenderer<Object> fieldValueRenderer ) {
		return new CustomFieldMetadata( name, fieldValueRenderer );
	}
}