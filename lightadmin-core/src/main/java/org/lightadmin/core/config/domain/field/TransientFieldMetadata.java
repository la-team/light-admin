package org.lightadmin.core.config.domain.field;

public class TransientFieldMetadata extends AbstractFieldMetadata {

	private final String property;

	public TransientFieldMetadata( final String name, final String property ) {
		super( name );
		this.property = property;
	}

	public String getProperty() {
		return property;
	}
}