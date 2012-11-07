package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.util.ClassUtils;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
public final class FilterMetadataUtils {

	private FilterMetadataUtils() {
	}

	public static FilterMetadata field( String fieldName ) {
		return new DefaultFilterMetadata( fieldName );
	}

	public static FilterMetadata decorateAttributeMetadata( FilterMetadata filterMetadata, DomainTypeAttributeMetadata attributeMetadata) {
		return new FilterMetadataDecorator( filterMetadata, attributeMetadata );
	}

	public static List<FilterMetadata> decorateAttributeMetadataAware( List<FilterMetadata> filterMetadatas, DomainTypeEntityMetadata entityMetadata ) {
		List<FilterMetadata> result = newLinkedList();

		for ( FilterMetadata filterMetadata : filterMetadatas ) {
			final DomainTypeAttributeMetadata attributeMetadata = entityMetadata.getAttribute( filterMetadata.getFieldName() );
			final FilterMetadata decoratedFilterMetadata = FilterMetadataUtils.decorateAttributeMetadata( filterMetadata, attributeMetadata );
			result.add( decoratedFilterMetadata );
		}

		return result;
	}

	private static class DefaultFilterMetadata implements FilterMetadata {

		private final String fieldName;

		public DefaultFilterMetadata( final String fieldName ) {
			this.fieldName = fieldName;
		}

		public String getFieldName() {
			return fieldName;
		}

		@Override
		public boolean equals( final Object o ) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || ClassUtils.isAssignableValue( FilterMetadata.class, o) ) {
				return false;
			}

			final FilterMetadata that = ( FilterMetadata ) o;

			if ( fieldName != null ? !fieldName.equals( that.getFieldName() ) : that.getFieldName() != null ) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return fieldName != null ? fieldName.hashCode() : 0;
		}
	}

	public static class FilterMetadataDecorator implements FilterMetadata {

		private final FilterMetadata filterMetadata;
		private DomainTypeAttributeMetadata attributeMetadata;

		private FilterMetadataDecorator( final FilterMetadata filterMetadata, final DomainTypeAttributeMetadata attributeMetadata ) {
			this.filterMetadata = filterMetadata;
			this.attributeMetadata = attributeMetadata;
		}

		public DomainTypeAttributeMetadata getAttributeMetadata() {
			return attributeMetadata;
		}

		@Override
		public String getFieldName() {
			return filterMetadata.getFieldName();
		}

		@Override
		public boolean equals( final Object o ) {
			return filterMetadata.equals( o );
		}

		@Override
		public int hashCode() {
			return filterMetadata.hashCode();
		}
	}
}