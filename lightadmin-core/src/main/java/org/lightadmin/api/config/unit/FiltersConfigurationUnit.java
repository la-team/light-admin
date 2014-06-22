package org.lightadmin.api.config.unit;

import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.handler.FilterHandler;
import org.lightadmin.core.config.domain.unit.visitor.VisitableConfigurationUnit;

public interface FiltersConfigurationUnit extends ConfigurationUnit, VisitableConfigurationUnit, Iterable<FilterMetadata> {

    void doWithFilters(FilterHandler<FilterMetadata> handler);
}