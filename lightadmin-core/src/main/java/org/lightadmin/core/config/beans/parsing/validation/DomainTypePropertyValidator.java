package org.lightadmin.core.config.beans.parsing.validation;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.data.mapping.PropertyPath;

import java.util.Iterator;

@SuppressWarnings( "unchecked" )
public class DomainTypePropertyValidator implements DomainTypePropertyValidatorInterface {

	private DomainTypeEntityMetadataResolver entityMetadataResolver;

	public DomainTypePropertyValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
	}

	@Override
	public boolean isInvalidProperty( String propertyName, DomainTypeEntityMetadata entityMetadata ) {
		return !isValidProperty( propertyName, entityMetadata );
	}

	@Override
	public boolean isValidProperty( String propertyName, DomainTypeEntityMetadata entityMetadata ) {
		try {
			final PropertyPath propertyPath = PropertyPath.from( propertyName, entityMetadata.getDomainType() );

			final Iterator<PropertyPath> iterator = propertyPath.iterator();

			while ( iterator.hasNext() ) {
				final PropertyPath property = iterator.next();
				if ( !entityMetadata.containsAttribute( property.getSegment() ) ) {
					return false;
				}

				if ( iterator.hasNext() ) {
					entityMetadata = entityMetadataResolver.resolveEntityMetadata( property.getType() );
					if ( entityMetadata == null ) {
						return false;
					}
				}
			}
			return true;
		} catch ( Exception ex ) {
			return false;
		}
	}
}