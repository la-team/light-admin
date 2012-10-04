package org.lightadmin.core.view.renderer;

import org.lightadmin.core.config.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

class ObjectAttributeRenderer extends AbstractAttributeRenderer {

	@Override
	protected Object evaluateValue( final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject ) {
		final DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forDomainType( attributeMetadata.getType() );
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess( domainTypeObject );
		if ( domainTypeConfiguration != null ) {
			final DomainTypeAttributeMetadata nameAttribute = domainTypeConfiguration.getDomainTypeEntityMetadata().getNameAttribute();
			if ( nameAttribute != null ) {
				return beanWrapper.getPropertyValue( nameAttribute.getName() );
			}
		}

		return beanWrapper.getPropertyValue( attributeMetadata.getName() );
	}
}