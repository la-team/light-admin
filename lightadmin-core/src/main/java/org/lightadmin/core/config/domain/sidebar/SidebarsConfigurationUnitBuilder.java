package org.lightadmin.core.config.domain.sidebar;

import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface SidebarsConfigurationUnitBuilder extends ConfigurationUnitBuilder<SidebarsConfigurationUnit> {

    SidebarsConfigurationUnitBuilder sidebar(final String sidebarJspPath);

    SidebarsConfigurationUnitBuilder sidebar(final String name, final String sidebarJspPath);
}