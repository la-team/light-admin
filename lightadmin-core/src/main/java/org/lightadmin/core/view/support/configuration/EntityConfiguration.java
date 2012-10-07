package org.lightadmin.core.view.support.configuration;

import com.google.common.base.Function;

public class EntityConfiguration {

	private final String nameField;
	private final Function<?, String> nameExtractor;

	EntityConfiguration( final String nameField, final Function<?, String> nameExtractor ) {
		this.nameField = nameField;
		this.nameExtractor = nameExtractor;
	}

	public String getNameField() {
		return nameField;
	}

	public Function<?, String> getNameExtractor() {
		return nameExtractor;
	}
}