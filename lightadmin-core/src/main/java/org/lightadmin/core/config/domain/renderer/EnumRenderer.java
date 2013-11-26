package org.lightadmin.core.config.domain.renderer;

import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.springframework.util.ObjectUtils;

public class EnumRenderer<F> implements FieldValueRenderer<F> {

    private final EnumElement[] elements;

    public EnumRenderer(EnumElement... elements) {
        this.elements = elements;
    }

    @Override
    public String apply(F input) {
        for (EnumElement elem : elements) {
            if (ObjectUtils.nullSafeEquals(elem.getValue(), input)) {
                return elem.getLabel();
            }
        }
        return (input != null) ? input.toString() : "";
    }

}
