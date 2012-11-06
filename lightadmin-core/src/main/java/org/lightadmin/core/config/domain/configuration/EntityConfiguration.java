package org.lightadmin.core.config.domain.configuration;

import java.io.Serializable;

public class EntityConfiguration implements Serializable {

	private transient final EntityNameExtractor<?> nameExtractor;

	EntityConfiguration( final EntityNameExtractor<?> nameExtractor ) {
		this.nameExtractor = nameExtractor;
	}

	public EntityNameExtractor getNameExtractor() {
		return nameExtractor;
	}
}