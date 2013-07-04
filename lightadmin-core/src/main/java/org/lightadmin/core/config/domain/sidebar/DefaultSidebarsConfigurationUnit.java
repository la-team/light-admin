package org.lightadmin.core.config.domain.sidebar;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.SIDEBARS;

public class DefaultSidebarsConfigurationUnit extends DomainTypeConfigurationUnit implements SidebarsConfigurationUnit {

    private final List<SidebarMetadata> sidabars;

    DefaultSidebarsConfigurationUnit(Class<?> domainType, List<SidebarMetadata> sidabars) {
        super(domainType);

        this.sidabars = newLinkedList(sidabars);
    }

    @Override
    public DomainConfigurationUnitType getDomainConfigurationUnitType() {
        return SIDEBARS;
    }

    @Override
    public List<SidebarMetadata> getSidebars() {
        return newLinkedList(sidabars);
    }
}