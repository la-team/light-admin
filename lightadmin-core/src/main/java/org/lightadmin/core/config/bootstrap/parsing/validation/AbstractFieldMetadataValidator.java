package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.apache.commons.lang.StringUtils;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Sets.newLinkedHashSet;

public abstract class AbstractFieldMetadataValidator<T extends FieldMetadata> implements FieldMetadataValidator<T> {

	private static final Pattern SPLITTER = Pattern.compile( "(?:[\\.]?([\\.]*?[^\\.]+))" );

	protected abstract String getFieldMetadataPropertyPath( final T fieldMetadata );

	@Override
	public boolean isValidFieldMetadata( final T fieldMetadata, final Class<?> domainType ) {
		final String propertyPath = getFieldMetadataPropertyPath( fieldMetadata );

		if ( StringUtils.isBlank( propertyPath ) ) {
			return false;
		}

		final Set<String> properties = properties( propertyPath );
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
				currentPropertyPath.append( "." ).append( propertiesIterator.next() );
			}

			if ( typeInformation.getProperty( currentPropertyPath.toString() ) == null ) {
				return false;
			}
		}

		return true;
	}

	private Set<String> properties( final String propertyPath ) {
		final Set<String> properties = newLinkedHashSet();

		final Matcher matcher = SPLITTER.matcher( propertyPath );
		while ( matcher.find() ) {
			properties.add( matcher.group( 1 ) );
		}
		return properties;
	}
}