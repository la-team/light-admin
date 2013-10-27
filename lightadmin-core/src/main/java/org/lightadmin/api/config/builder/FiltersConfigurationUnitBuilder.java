package org.lightadmin.api.config.builder;

import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.lightadmin.core.config.domain.renderer.Renderer;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface FiltersConfigurationUnitBuilder extends ConfigurationUnitBuilder<FiltersConfigurationUnit> {

    FiltersConfigurationUnitBuilder filter(final String filterName, String fieldName);

    FiltersConfigurationUnitBuilder filters(final FilterMetadata... filters);

    FiltersConfigurationUnitBuilder renderer(final Renderer renderer);

}