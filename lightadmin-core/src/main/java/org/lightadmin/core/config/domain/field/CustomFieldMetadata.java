package org.lightadmin.core.config.domain.field;

import org.lightadmin.api.config.utils.FieldValueRenderer;

public class CustomFieldMetadata extends AbstractFieldMetadata {

    public CustomFieldMetadata(final String name, final FieldValueRenderer<Object> renderer) {
        super(name);
        this.renderer = renderer;
    }

    public FieldValueRenderer getRenderer() {
        return renderer;
    }
}