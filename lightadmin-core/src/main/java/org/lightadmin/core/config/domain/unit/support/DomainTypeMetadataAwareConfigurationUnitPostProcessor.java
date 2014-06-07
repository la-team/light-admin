package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.metamodel.PersistentEntityAware;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.ClassUtils;

public class DomainTypeMetadataAwareConfigurationUnitPostProcessor extends EntityMetadataResolverAwareConfigurationUnitPostProcessor {

    public DomainTypeMetadataAwareConfigurationUnitPostProcessor(final MappingContext mappingContext) {
        super(mappingContext);
    }

    @Override
    public ConfigurationUnit postProcess(final ConfigurationUnit configurationUnit, ConfigurationUnits configurationUnits) {
        if (ClassUtils.isAssignableValue(PersistentEntityAware.class, configurationUnit)) {
            final PersistentEntity persistentEntity = getPersistentEntity(configurationUnit);

            ((PersistentEntityAware) configurationUnit).setPersistentEntity(persistentEntity);
        }
        return configurationUnit;
    }
}