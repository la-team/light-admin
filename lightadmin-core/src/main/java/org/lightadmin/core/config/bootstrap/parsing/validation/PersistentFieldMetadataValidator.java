package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;

import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.UNKNOWN;

class PersistentFieldMetadataValidator implements FieldMetadataValidator<PersistentFieldMetadata> {

	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	PersistentFieldMetadataValidator( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public boolean isValidFieldMetadata( final PersistentFieldMetadata fieldMetadata, final Class<?> domainType ) {
		final DomainTypeEntityMetadata domainTypeEntityMetadata = entityMetadataResolver.resolveEntityMetadata( domainType );

		final DomainTypeAttributeMetadata attributeMetadata = domainTypeEntityMetadata.getAttribute( fieldMetadata.getField() );

		return attributeMetadata != null && attributeMetadata.getAttributeType() != UNKNOWN;
	}
}