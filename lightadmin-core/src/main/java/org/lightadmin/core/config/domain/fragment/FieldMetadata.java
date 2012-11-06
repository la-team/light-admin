package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.springframework.util.Assert;

import java.io.Serializable;

public class FieldMetadata implements Serializable {

	private final String fieldName;

	private String alias;

	private FieldValueRenderer renderer;

	public FieldMetadata( final String fieldName ) {
		Assert.notNull( fieldName );

		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias( final String alias ) {
		this.alias = alias;
	}

	public FieldValueRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer( final FieldValueRenderer renderer ) {
		this.renderer = renderer;
	}

	@Override
	public boolean equals( final Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final FieldMetadata that = ( FieldMetadata ) o;

		return fieldName.equals( that.fieldName );
	}

	@Override
	public int hashCode() {
		return fieldName.hashCode();
	}
}