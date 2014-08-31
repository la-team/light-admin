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
import org.lightadmin.core.config.domain.unit.visitor.*;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;

import static org.springframework.util.ClassUtils.isAssignableValue;

public class VisitableConfigurationUnitPostProcessor extends MappingContextAwareConfigurationUnitPostProcessor {

    public VisitableConfigurationUnitPostProcessor(final MappingContext mappingContext) {
        super(mappingContext);
    }

    @Override
    public ConfigurationUnit postProcess(final ConfigurationUnit configurationUnit, ConfigurationUnits configurationUnits) {
        if (isAssignableValue(VisitableConfigurationUnit.class, configurationUnit)) {
            VisitableConfigurationUnit visitableConfigurationUnit = (VisitableConfigurationUnit) configurationUnit;

            ConfigurationUnitVisitor<VisitableConfigurationUnit>[] configurationUnitVisitors = configurationUnitVisitor(configurationUnit);
            for (ConfigurationUnitVisitor<VisitableConfigurationUnit> configurationUnitVisitor : configurationUnitVisitors) {
                configurationUnitVisitor.visit(visitableConfigurationUnit);
            }
        }
        return configurationUnit;
    }

    @SuppressWarnings("unchecked")
    private ConfigurationUnitVisitor<VisitableConfigurationUnit>[] configurationUnitVisitor(ConfigurationUnit configurationUnit) {
        final PersistentEntity persistentEntity = getPersistentEntity(configurationUnit);
        return new ConfigurationUnitVisitor[]{
                new EntityMetadataConfigurationUnitVisitor(persistentEntity),
                new FieldSetConfigurationUnitVisitor(persistentEntity),
                new FiltersConfigurationUnitVisitor(persistentEntity)
        };
    }
}