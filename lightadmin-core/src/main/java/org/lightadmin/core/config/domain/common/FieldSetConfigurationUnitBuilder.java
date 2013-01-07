package org.lightadmin.core.config.domain.common;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;

public interface FieldSetConfigurationUnitBuilder extends ConfigurationUnitBuilder<FieldSetConfigurationUnit> {

	FieldSetConfigurationUnitBuilder field( String fieldName );

	FieldSetConfigurationUnitBuilder caption( String caption );

	FieldSetConfigurationUnitBuilder dynamic( String expression );

	FieldSetConfigurationUnitBuilder renderable( FieldValueRenderer<?> renderer );

}