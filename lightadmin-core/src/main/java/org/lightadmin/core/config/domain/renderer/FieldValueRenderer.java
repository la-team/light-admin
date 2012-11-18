package org.lightadmin.core.config.domain.renderer;

import org.lightadmin.core.util.Transformer;

import java.io.Serializable;

public interface FieldValueRenderer<F> extends Renderer, Transformer<F, String>, Serializable {

}