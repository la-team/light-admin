package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public abstract class EntityNameExtractorFactory {

	public static EntityNameExtractor<?> forPersistentEntity( DomainTypeEntityMetadata entityMetadata ) {
		return new PersistentEntityNameExtractor( entityMetadata );
	}

	public static EntityNameExtractor<?> forNamedPersistentEntity( String name ) {
		return new NamedPersistentEntityExtractor( name );
	}

	private static class PersistentEntityNameExtractor implements EntityNameExtractor<Object> {

		private final DomainTypeEntityMetadata entityMetadata;

		public PersistentEntityNameExtractor( final DomainTypeEntityMetadata entityMetadata ) {
			this.entityMetadata = entityMetadata;
		}

		@Override
		public String apply( final Object entity ) {
			return String.format( "%s #%s", entityMetadata.getEntityName(), entityMetadata.getIdAttribute().getValue( entity ) );
		}
	}

	private static class NamedPersistentEntityExtractor implements EntityNameExtractor<Object> {

		private final String nameField;

		private NamedPersistentEntityExtractor( final String nameField ) {
			this.nameField = nameField;
		}

		@Override
		public String apply( final Object input ) {
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess( input );
			return beanWrapper.getPropertyValue( nameField ).toString();
		}
	}
}