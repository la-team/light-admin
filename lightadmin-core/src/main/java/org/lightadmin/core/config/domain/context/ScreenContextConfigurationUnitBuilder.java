package org.lightadmin.core.config.domain.context;

import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface ScreenContextConfigurationUnitBuilder extends ConfigurationUnitBuilder<ScreenContextConfigurationUnit> {

	ScreenContextConfigurationUnitBuilder screenName( String screenName );

}