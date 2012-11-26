package org.lightadmin.core.config.domain.show;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface ShowViewConfigurationUnitBuilder extends ConfigurationUnitBuilder<ShowViewConfigurationUnit>  {

	ShowViewConfigurationUnitBuilder field( String fieldName );

	ShowViewConfigurationUnitBuilder alias( String alias );

	ShowViewConfigurationUnitBuilder renderer( FieldValueRenderer<?> renderer );

}