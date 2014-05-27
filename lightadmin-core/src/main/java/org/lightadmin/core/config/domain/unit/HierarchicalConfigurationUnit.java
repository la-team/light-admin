package org.lightadmin.core.config.domain.unit;

public interface HierarchicalConfigurationUnit extends ConfigurationUnit {

    DomainConfigurationUnitType getParentUnitType();

    void setParentUnit(ConfigurationUnit parentUnit);

}
