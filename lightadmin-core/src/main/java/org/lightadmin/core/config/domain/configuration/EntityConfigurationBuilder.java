package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.domain.support.Builder;

public interface EntityConfigurationBuilder extends Builder<EntityConfiguration> {

	EntityConfigurationBuilder nameField( String nameField );

	EntityConfigurationBuilder nameExtractor( EntityNameExtractor<?> nameExtractor );

}