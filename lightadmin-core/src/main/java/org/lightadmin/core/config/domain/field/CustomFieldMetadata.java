package org.lightadmin.core.config.domain.field;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;

public class CustomFieldMetadata extends AbstractFieldMetadata {

	private transient final FieldValueRenderer<Object> renderer;

	public CustomFieldMetadata( final String name, final FieldValueRenderer<Object> renderer ) {
		super( name );
		this.renderer = renderer;
	}

	public FieldValueRenderer getRenderer() {
		return renderer;
	}
}