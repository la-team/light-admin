package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.util.Transformer;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class DefaultEntityConfigurationBuilder implements EntityConfigurationBuilder {

	private Transformer<?, String> nameExtractor = new ObjectNameExtractor();

	@Override
	public EntityConfigurationBuilder nameField( final String nameField ) {
		this.nameExtractor = new DomainTypeObjectNameExtractor( nameField );
		return this;
	}

	@Override
	public EntityConfigurationBuilder nameExtractor( final Transformer<?, String> nameExtractor ) {
		this.nameExtractor = nameExtractor;
		return this;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public EntityConfiguration build() {
		return new EntityConfiguration( ( Transformer<Object, String> ) nameExtractor );
	}

	private static class ObjectNameExtractor implements Transformer<Object, String> {

		@Override
		public String apply( final Object input ) {
			return String.format( "%s", input.getClass().getSimpleName() );
		}
	}

	private static class DomainTypeObjectNameExtractor implements Transformer<Object, String> {

		private final String nameField;

		private DomainTypeObjectNameExtractor( final String nameField ) {
			this.nameField = nameField;
		}

		@Override
		public String apply( final Object input ) {
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess( input );
			return beanWrapper.getPropertyValue( nameField ).toString();
		}
	}
}