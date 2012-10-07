package org.lightadmin.core.view.support.configuration;

import com.google.common.base.Function;

public class DefaultEntityConfigurationBuilder implements EntityConfigurationBuilder {

	private static final String DEFAULT_NAME_FIELD = "name";

	private String nameField = DEFAULT_NAME_FIELD;

	private Function<?, String> nameExtractor = new Function<Object, String>() {
		@Override
		public String apply( final Object input ) {
			return String.format( "%s", input.getClass().getSimpleName() );
		}
	};

	@Override
	public EntityConfigurationBuilder nameField( final String nameField ) {
		this.nameField = nameField;
		return this;
	}

	@Override
	public EntityConfigurationBuilder nameExtractor( final Function<?, String> nameExtractor ) {
		this.nameExtractor = nameExtractor;
		return this;
	}

	@Override
	public EntityConfiguration build() {
		return new EntityConfiguration( nameField, nameExtractor );
	}
}