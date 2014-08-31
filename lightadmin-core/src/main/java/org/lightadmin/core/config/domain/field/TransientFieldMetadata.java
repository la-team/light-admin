package org.lightadmin.core.config.domain.field;

import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

public class TransientFieldMetadata extends AbstractFieldMetadata {

    private final String property;

    public TransientFieldMetadata(final String name, final String property) {
        super(name);
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public Object getValue(Object entity) {
        return new DirectFieldAccessFallbackBeanWrapper(entity).getPropertyValue(property);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}