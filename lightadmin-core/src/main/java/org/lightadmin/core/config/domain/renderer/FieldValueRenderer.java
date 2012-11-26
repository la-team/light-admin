package org.lightadmin.core.config.domain.renderer;

import org.lightadmin.core.util.Transformer;

import javax.annotation.Nullable;
import java.io.Serializable;

public interface FieldValueRenderer<F> extends Renderer, Transformer<F, String>, Serializable {

	@Override
	String apply( @Nullable F input );
}