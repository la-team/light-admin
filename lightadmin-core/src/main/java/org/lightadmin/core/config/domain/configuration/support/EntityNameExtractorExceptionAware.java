package org.lightadmin.core.config.domain.configuration.support;

import javax.annotation.Nullable;

public class EntityNameExtractorExceptionAware<F> implements EntityNameExtractor<F> {

	private final EntityNameExtractor<F> entityNameExtractor;

	private EntityNameExtractorExceptionAware( final EntityNameExtractor<F> entityNameExtractor ) {
		this.entityNameExtractor = entityNameExtractor;
	}

	public static <F> EntityNameExtractor<F> exceptionAwareNameExtractor( final EntityNameExtractor<F> entityNameExtractor ) {
		return new EntityNameExtractorExceptionAware<F>( entityNameExtractor );
	}

	@Override
	public String apply( @Nullable final F input ) {
		try {
			return entityNameExtractor.apply( input );
		} catch ( Exception ex ) {
			return "";
		}
	}
}