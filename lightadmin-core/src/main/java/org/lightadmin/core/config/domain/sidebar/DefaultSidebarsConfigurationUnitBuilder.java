package org.lightadmin.core.config.domain.sidebar;

import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DefaultSidebarsConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<SidebarsConfigurationUnit> implements SidebarsConfigurationUnitBuilder {

    private final List<SidebarMetadata> sidebars = newLinkedList();

    public DefaultSidebarsConfigurationUnitBuilder(Class<?> domainType) {
        super(domainType);
    }

    @Override
    public SidebarsConfigurationUnitBuilder sidebar(String sidebarJspPath) {
        sidebars.add(new SidebarMetadata(sidebarJspPath));

        return this;
    }

    @Override
    public SidebarsConfigurationUnitBuilder sidebar(String name, String sidebarJspPath) {
        sidebars.add(new SidebarMetadata(name, sidebarJspPath));

        return this;
    }

    @Override
    public SidebarsConfigurationUnit build() {
        return new DefaultSidebarsConfigurationUnit(getDomainType(), sidebars);
    }
}