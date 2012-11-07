package org.lightadmin.core.config.domain.context;

import org.lightadmin.core.config.domain.support.ConfigurationUnitBuilder;

public interface ScreenContextConfigurationUnitBuilder extends ConfigurationUnitBuilder<ScreenContextConfigurationUnit> {

	ScreenContextConfigurationUnitBuilder screenName( String screenName );

	ScreenContextConfigurationUnitBuilder menuName( String menuName );

}