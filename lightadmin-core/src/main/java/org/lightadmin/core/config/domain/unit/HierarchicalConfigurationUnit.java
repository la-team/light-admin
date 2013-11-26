package org.lightadmin.core.config.domain.unit;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;

public interface HierarchicalConfigurationUnit extends ConfigurationUnit {

    DomainConfigurationUnitType getParentUnitType();

    void setParentUnit(ConfigurationUnit parentUnit);

}
