package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.domain.support.ConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.support.EntityNameExtractor;

public interface EntityMetadataConfigurationUnitBuilder extends ConfigurationUnitBuilder<EntityMetadataConfigurationUnit> {

	EntityMetadataConfigurationUnitBuilder nameField( String nameField );

	EntityMetadataConfigurationUnitBuilder nameExtractor( EntityNameExtractor<?> nameExtractor );

}