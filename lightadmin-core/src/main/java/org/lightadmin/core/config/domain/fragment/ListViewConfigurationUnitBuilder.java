package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface ListViewConfigurationUnitBuilder extends ConfigurationUnitBuilder<ListViewConfigurationUnit> {

	ListViewConfigurationUnitBuilder field( String fieldName );

	ListViewConfigurationUnitBuilder alias( String alias );

	ListViewConfigurationUnitBuilder attribute( String fieldName );

	ListViewConfigurationUnitBuilder renderer( FieldValueRenderer<?> renderer );

}