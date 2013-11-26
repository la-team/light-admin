package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.HierarchicalConfigurationUnit;

public class HierarchicalConfigurationPostProcessor implements ConfigurationUnitPostProcessor {

    @Override
    public ConfigurationUnit postProcess(ConfigurationUnit configurationUnit, ConfigurationUnits allUnits) {
        if (!(configurationUnit instanceof HierarchicalConfigurationUnit)) {
            return configurationUnit;
        }
        HierarchicalConfigurationUnit hierarchicalUnit = (HierarchicalConfigurationUnit) configurationUnit;
        if (hierarchicalUnit.getParentUnitType() != null) {
            ConfigurationUnit parentUnit = allUnits.forDomainUnitType(hierarchicalUnit.getParentUnitType());
            hierarchicalUnit.setParentUnit(parentUnit);
        }
        return configurationUnit;
    }

}
