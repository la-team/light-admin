package org.lightadmin.core.config.domain.configuration.support;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.util.Transformer;

import javax.annotation.Nullable;

public class ExceptionAwareTransformer implements Transformer<Object, String> {

	private final Transformer<Object, String> transformer;

	private ExceptionAwareTransformer( final Transformer<Object, String> transformer ) {
		this.transformer = transformer;
	}

	public static Transformer<Object, String> exceptionAwareNameExtractor( final EntityNameExtractor<Object> entityNameExtractor ) {
		return new ExceptionAwareTransformer( entityNameExtractor );
	}

	public static Transformer<Object, String> exceptionAwareFieldValueRenderer( final FieldValueRenderer<Object> fieldValueRenderer ) {
		return new ExceptionAwareTransformer( fieldValueRenderer );
	}

	@Nullable
	@Override
	public String apply( @Nullable final Object input ) {
		try {
			return transformer.apply( input );
		} catch ( Exception ex ) {
			return "";
		}
	}
}