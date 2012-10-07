package org.lightadmin.core.view.support.configuration;

import com.google.common.base.Function;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class DefaultEntityConfigurationBuilder implements EntityConfigurationBuilder {

	private Function<?, String> nameExtractor = new ObjectNameExtractor();

	@Override
	public EntityConfigurationBuilder nameField( final String nameField ) {
		this.nameExtractor = new DomainTypeObjectNameExtractor( nameField );
		return this;
	}

	@Override
	public EntityConfigurationBuilder nameExtractor( final Function<?, String> nameExtractor ) {
		this.nameExtractor = nameExtractor;
		return this;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public EntityConfiguration build() {
		return new EntityConfiguration( ( Function<Object, String> ) nameExtractor );
	}

	private static class ObjectNameExtractor implements Function<Object, String> {

		@Override
		public String apply( final Object input ) {
//			String.format( "%s #%s", domainTypeConfiguration.getDomainTypeName(), entityMetadata.getIdAttribute().getValue( source ).toString()

			return String.format( "%s", input.getClass().getSimpleName() );
		}
	}

	private static class DomainTypeObjectNameExtractor implements Function<Object, String> {

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