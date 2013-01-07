package org.lightadmin.core.config.domain.common;

import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;

public interface PersistentFieldSetConfigurationUnitBuilder extends ConfigurationUnitBuilder<FieldSetConfigurationUnit> {

	PersistentFieldSetConfigurationUnitBuilder field( String fieldName );

	PersistentFieldSetConfigurationUnitBuilder caption( String caption );

}