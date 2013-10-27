package org.lightadmin.core.view.renderer;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.beans.PropertyAccessorFactory;

class ObjectAttributeRenderer extends AbstractAttributeRenderer {

    @Override
    protected Object evaluateValue(final DomainTypeAttributeMetadata attributeMetadata, final Object domainTypeObject) {
        return PropertyAccessorFactory.forBeanPropertyAccess(domainTypeObject).getPropertyValue(attributeMetadata.getName());
    }
}