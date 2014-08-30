package org.lightadmin.core.config.domain.field;

import org.lightadmin.api.config.utils.FieldValueRenderer;

import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareFieldValueRenderer;

public class CustomFieldMetadata extends AbstractFieldMetadata {

    public CustomFieldMetadata(final String name, final FieldValueRenderer<Object> renderer) {
        super(name);
        this.renderer = renderer;
    }

    @Override
    public FieldValueRenderer getRenderer() {
        return renderer;
    }

    @Override
    public Object getValue(Object entity) {
        return exceptionAwareFieldValueRenderer(this.renderer).apply(entity);
    }
}