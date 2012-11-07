package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

public interface EntityMetadataConfigurationUnit extends ConfigurationUnit {

	EntityNameExtractor getNameExtractor();
}