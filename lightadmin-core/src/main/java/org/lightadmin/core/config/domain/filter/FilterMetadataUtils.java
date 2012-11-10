package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.util.Assert;

public final class FilterMetadataUtils {

	private FilterMetadataUtils() {
	}

	public static FilterMetadata field( String fieldName ) {
		return new DefaultFilterMetadata( fieldName );
	}

	private static class DefaultFilterMetadata implements FilterMetadata {

		private final String fieldName;

		private transient DomainTypeAttributeMetadata attributeMetadata;

		public DefaultFilterMetadata( final String fieldName ) {
			Assert.notNull( fieldName );
			this.fieldName = fieldName;
		}

		public String getFieldName() {
			return fieldName;
		}

		@Override
		public void setAttributeMetadata( final DomainTypeAttributeMetadata attributeMetadata ) {
			this.attributeMetadata = attributeMetadata;
		}

		@Override
		public DomainTypeAttributeMetadata getAttributeMetadata() {
			return attributeMetadata;
		}

		@Override
		public boolean equals( final Object o ) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}

			final DefaultFilterMetadata that = ( DefaultFilterMetadata ) o;

			if ( attributeMetadata != null ? !attributeMetadata.equals( that.attributeMetadata ) : that.attributeMetadata != null ) {
				return false;
			}
			if ( !fieldName.equals( that.fieldName ) ) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = fieldName.hashCode();
			result = 31 * result + ( attributeMetadata != null ? attributeMetadata.hashCode() : 0 );
			return result;
		}
	}
}