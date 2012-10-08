package org.lightadmin.core.view.renderer;

import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.beans.PropertyAccessorFactory;

class ObjectAttributeRenderer extends AbstractAttributeRenderer {

	// TODO: max: Please consider Persistent entity, DomainType entity and Simple Object (toString)
	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	ObjectAttributeRenderer( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
	}

	@Override
	protected Object evaluateValue( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject ) {
		final DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forDomainType( attributeMetadata.getType() );

		final Object propertyValue = PropertyAccessorFactory.forBeanPropertyAccess( domainTypeObject ).getPropertyValue( attributeMetadata.getName() );

		if ( domainTypeConfiguration != null ) {
			return domainTypeConfiguration.getEntityConfiguration().getNameExtractor().apply( propertyValue );
		}

		return propertyValue;
	}
}