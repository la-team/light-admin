package org.lightadmin.core.util;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.data.mapping.PropertyPath;

import java.util.Iterator;

@SuppressWarnings( "unchecked" )
public abstract class DomainTypeBeanUtils {

	public static boolean isInvalidProperty( String propertyName, DomainTypeEntityMetadata entityMetadata, DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		try {
			final PropertyPath propertyPath = PropertyPath.from( propertyName, entityMetadata.getDomainType() );

			final Iterator<PropertyPath> iterator = propertyPath.iterator();

			while ( iterator.hasNext() ) {
				final PropertyPath property = iterator.next();
				if ( !entityMetadata.containsAttribute( property.getSegment() ) ) {
					return true;
				}

				if ( iterator.hasNext() ) {
					final Class<?> propertyType = property.getType();
					entityMetadata = entityMetadataResolver.resolveEntityMetadata( propertyType );
					if ( entityMetadata == null ) {
						return true;
					}
				}
			}
			return false;
		} catch ( Exception ex ) {
			return true;
		}
	}
}