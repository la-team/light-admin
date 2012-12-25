package org.lightadmin.core.config.domain.form;

import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;

public interface FormViewConfigurationUnitBuilder extends ConfigurationUnitBuilder<FieldSetConfigurationUnit> {

	FormViewConfigurationUnitBuilder field( String fieldName );

	FormViewConfigurationUnitBuilder alias( String alias );

}
