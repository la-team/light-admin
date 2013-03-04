package org.lightadmin.core.config.domain.configuration.support;

import org.lightadmin.core.util.Transformer;

import javax.annotation.Nullable;

public interface EntityNameExtractor<F> extends Transformer<F, String> {

	@Override
	String apply( @Nullable F input );
}