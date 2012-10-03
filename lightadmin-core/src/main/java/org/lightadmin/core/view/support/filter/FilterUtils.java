package org.lightadmin.core.view.support.filter;

import com.google.common.base.Predicate;
import org.springframework.data.rest.repository.AttributeMetadata;

public final class FilterUtils {

	private FilterUtils() {
	}

	public static Filter field( String fieldName ) {
		return new DefaultFilter( fieldName );
	}

	public static Predicate fieldEq( final String fieldName, final Object value ) {
		return null;
	}

	private static class DefaultFilter implements Filter {

		private final String fieldName;

		private AttributeMetadata attributeMetadata;

		public DefaultFilter( final String fieldName ) {
			this.fieldName = fieldName;
		}

		public String getFieldName() {
			return fieldName;
		}

		@Override
		public void setAttributeMetadata( final AttributeMetadata attributeMetadata ) {
			this.attributeMetadata = attributeMetadata;
		}

		@Override
		public AttributeMetadata getAttributeMetadata() {
			return attributeMetadata;
		}
	}
}