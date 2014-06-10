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