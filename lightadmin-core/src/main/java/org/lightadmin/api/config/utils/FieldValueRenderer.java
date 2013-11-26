package org.lightadmin.api.config.utils;

import org.lightadmin.core.config.domain.renderer.Renderer;
import org.lightadmin.core.util.Transformer;

import java.io.Serializable;

public interface FieldValueRenderer<F> extends Renderer, Transformer<F, String>, Serializable {

    @Override
    String apply(F input);
}