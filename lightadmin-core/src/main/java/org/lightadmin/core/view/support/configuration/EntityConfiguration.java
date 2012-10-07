package org.lightadmin.core.view.support.configuration;

import com.google.common.base.Function;

public class EntityConfiguration {

	private final Function<Object, String> nameExtractor;

	EntityConfiguration( final Function<Object, String> nameExtractor ) {
		this.nameExtractor = nameExtractor;
	}

	public Function<Object, String> getNameExtractor() {
		return nameExtractor;
	}
}