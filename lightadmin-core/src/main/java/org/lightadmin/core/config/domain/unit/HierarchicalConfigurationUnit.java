package org.lightadmin.core.config.domain.unit;

import org.lightadmin.core.config.domain.unit.visitor.VisitableConfigurationUnit;

public interface HierarchicalConfigurationUnit extends ConfigurationUnit, VisitableConfigurationUnit {

    DomainConfigurationUnitType getParentUnitType();

}