package org.lightadmin.api.config.unit;

import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

public interface FiltersConfigurationUnit extends ConfigurationUnit, Iterable<FilterMetadata> {

}