package org.lightadmin.api.config.builder;

import org.lightadmin.api.config.unit.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface ScreenContextConfigurationUnitBuilder extends ConfigurationUnitBuilder<ScreenContextConfigurationUnit> {

    ScreenContextConfigurationUnitBuilder screenName(String screenName);

}