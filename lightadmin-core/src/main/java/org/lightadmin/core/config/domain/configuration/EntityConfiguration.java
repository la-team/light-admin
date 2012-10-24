package org.lightadmin.core.config.domain.configuration;

public class EntityConfiguration {

	private final EntityNameExtractor<?> nameExtractor;

	EntityConfiguration( final EntityNameExtractor<?> nameExtractor ) {
		this.nameExtractor = nameExtractor;
	}

	public EntityNameExtractor getNameExtractor() {
		return nameExtractor;
	}
}