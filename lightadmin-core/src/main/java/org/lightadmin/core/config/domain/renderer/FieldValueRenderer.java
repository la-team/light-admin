package org.lightadmin.core.config.domain.renderer;

import org.lightadmin.core.util.Transformer;

public interface FieldValueRenderer<F> extends Renderer, Transformer<F, String> {

}