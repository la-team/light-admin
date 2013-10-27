package org.lightadmin.api.config.builder;

import org.lightadmin.api.config.unit.SidebarsConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface SidebarsConfigurationUnitBuilder extends ConfigurationUnitBuilder<SidebarsConfigurationUnit> {

    SidebarsConfigurationUnitBuilder sidebar(final String sidebarJspPath);

    SidebarsConfigurationUnitBuilder sidebar(final String name, final String sidebarJspPath);
}