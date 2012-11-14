package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.domain.renderer.Renderer;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface FiltersConfigurationUnitBuilder extends ConfigurationUnitBuilder<FiltersConfigurationUnit> {

	FiltersConfigurationUnitBuilder filter( final String filterName, String fieldName );

	FiltersConfigurationUnitBuilder renderer( final Renderer renderer );

}