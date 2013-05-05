package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface EntityMetadataConfigurationUnitBuilder extends ConfigurationUnitBuilder<EntityMetadataConfigurationUnit> {

	EntityMetadataConfigurationUnitBuilder nameField( String nameField );

	EntityMetadataConfigurationUnitBuilder nameExtractor( EntityNameExtractor<?> nameExtractor );

	EntityMetadataConfigurationUnitBuilder singularName( String singularName );

	EntityMetadataConfigurationUnitBuilder pluralName( String pluralName );

}