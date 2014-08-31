/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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