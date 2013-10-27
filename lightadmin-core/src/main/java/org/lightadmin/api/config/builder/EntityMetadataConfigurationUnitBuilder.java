package org.lightadmin.api.config.builder;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface EntityMetadataConfigurationUnitBuilder extends ConfigurationUnitBuilder<EntityMetadataConfigurationUnit> {

    EntityMetadataConfigurationUnitBuilder nameField(String nameField);

    EntityMetadataConfigurationUnitBuilder nameExtractor(EntityNameExtractor<?> nameExtractor);

    EntityMetadataConfigurationUnitBuilder singularName(String singularName);

    EntityMetadataConfigurationUnitBuilder pluralName(String pluralName);

}