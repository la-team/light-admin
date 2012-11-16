package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.apache.commons.lang.StringUtils;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public abstract class AbstractFieldMetadataValidator<T extends FieldMetadata> implements FieldMetadataValidator<T> {

	private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile( "([a-zA-Z_$][a-zA-Z\\d_$]*)(\\.[a-zA-Z_$][a-zA-Z\\d_$]*)*" );

	private static final String PROPERTY_SEPARATOR = ".";

	protected abstract String getFieldMetadataPropertyPath( final T fieldMetadata );

	@Override
	public boolean isValidFieldMetadata( final T fieldMetadata, final Class<?> domainType ) {
		final String propertyPath = getFieldMetadataPropertyPath( fieldMetadata );

		if ( StringUtils.isBlank( propertyPath ) ) {
			return false;
		}

		final List<String> properties = properties( propertyPath );
		if ( properties.isEmpty() ) {
			return false;
		}

		final TypeInformation<?> typeInformation = ClassTypeInformation.from( domainType );

		final Iterator<String> propertiesIterator = properties.iterator();

		StringBuilder currentPropertyPath = null;
		while ( propertiesIterator.hasNext() ) {
			if ( currentPropertyPath == null ) {
				currentPropertyPath = new StringBuilder( propertiesIterator.next() );
			} else {
				currentPropertyPath.append( PROPERTY_SEPARATOR ).append( propertiesIterator.next() );
			}

			if ( typeInformation.getProperty( currentPropertyPath.toString() ) == null ) {
				return false;
			}
		}

		return true;
	}

	private List<String> properties( final String propertyPath ) {
		if ( !PROPERTY_NAME_PATTERN.matcher( propertyPath ).matches() ) {
			return Collections.emptyList();
		}
		return newArrayList( StringUtils.split( propertyPath, PROPERTY_SEPARATOR ) );
	}
}