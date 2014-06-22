package org.lightadmin.core.config.domain.unit.processor;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.HierarchicalConfigurationUnit;
import org.lightadmin.core.config.domain.unit.visitor.ConfigurationUnitVisitor;
import org.lightadmin.core.config.domain.unit.visitor.HierarchicalConfigurationUnitVisitor;
import org.lightadmin.core.config.domain.unit.visitor.VisitableConfigurationUnit;

public class HierarchicalConfigurationPostProcessor implements ConfigurationUnitPostProcessor {

    @Override
    public ConfigurationUnit postProcess(ConfigurationUnit configurationUnit, ConfigurationUnits allUnits) {
        if (!(configurationUnit instanceof HierarchicalConfigurationUnit)) {
            return configurationUnit;
        }

        HierarchicalConfigurationUnit hierarchicalUnit = (HierarchicalConfigurationUnit) configurationUnit;
        if (hierarchicalUnit.getParentUnitType() != null) {
            ConfigurationUnit parentUnit = allUnits.forDomainUnitType(hierarchicalUnit.getParentUnitType());

            hierarchicalConfigurationUnitVisitor(parentUnit).visit(hierarchicalUnit);
        }
        return configurationUnit;
    }

    private ConfigurationUnitVisitor<VisitableConfigurationUnit> hierarchicalConfigurationUnitVisitor(ConfigurationUnit parentUnit) {
        return new HierarchicalConfigurationUnitVisitor(parentUnit);
    }
}