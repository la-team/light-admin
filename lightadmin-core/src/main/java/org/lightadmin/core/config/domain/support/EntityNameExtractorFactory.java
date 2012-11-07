package org.lightadmin.core.config.domain.support;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.Serializable;

public abstract class EntityNameExtractorFactory {

	public static EntityNameExtractor<?> forSimpleObject() {
		return new SimpleObjectNameExtractor();
	}

	public static EntityNameExtractor<?> forPersistentEntity( DomainTypeEntityMetadata entityMetadata ) {
		return new PersistentEntityNameExtractor( entityMetadata );
	}

	public static EntityNameExtractor<?> forNamedPersistentEntity( String name ) {
		return new NamedPersistentEntityNameExtractor( name );
	}

	private static class SimpleObjectNameExtractor implements EntityNameExtractor<Object>, Serializable {

		@Override
		public String apply( final Object input ) {
			return input.getClass().getSimpleName();
		}
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

	public static class NamedPersistentEntityNameExtractor implements EntityNameExtractor<Object>, Serializable {

		private final String nameField;

		private NamedPersistentEntityNameExtractor( final String nameField ) {
			this.nameField = nameField;
		}

		@Override
		public String apply( final Object input ) {
			BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess( input );
			return beanWrapper.getPropertyValue( nameField ).toString();
		}
	}
}