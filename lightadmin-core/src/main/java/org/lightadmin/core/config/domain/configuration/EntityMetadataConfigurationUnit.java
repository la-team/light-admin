package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.domain.support.ConfigurationUnit;
import org.lightadmin.core.config.domain.support.EntityNameExtractor;

public interface EntityMetadataConfigurationUnit extends ConfigurationUnit {

	EntityNameExtractor getNameExtractor();
}