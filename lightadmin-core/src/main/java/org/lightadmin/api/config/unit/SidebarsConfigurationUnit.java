package org.lightadmin.api.config.unit;

import org.lightadmin.core.config.domain.sidebar.SidebarMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

import java.util.List;

public interface SidebarsConfigurationUnit extends ConfigurationUnit {

    List<SidebarMetadata> getSidebars();
}