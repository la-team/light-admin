package org.lightadmin.core.config.domain.fragment.field;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;

public class CustomFieldMetadata extends AbstractFieldMetadata {

	private transient final FieldValueRenderer<Object> renderer;

	protected CustomFieldMetadata( final String name, final FieldValueRenderer<Object> renderer ) {
		super( name );
		this.renderer = renderer;
	}

	public FieldValueRenderer getRenderer() {
		return renderer;
	}

	@Override
	public boolean isSortable() {
		return true;
	}
}