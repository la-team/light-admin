package org.lightadmin.core.config.domain.sidebar;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

import java.util.List;

public interface SidebarsConfigurationUnit extends ConfigurationUnit {

    List<SidebarMetadata> getSidebars();
}