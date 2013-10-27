package org.lightadmin.api.config.unit;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

public interface EntityMetadataConfigurationUnit extends ConfigurationUnit {

    EntityNameExtractor getNameExtractor();

    String getSingularName();

    String getPluralName();
}