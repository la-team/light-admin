package org.lightadmin.core.config.domain.unit.processor;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;

public abstract class MappingContextAwareConfigurationUnitPostProcessor implements ConfigurationUnitPostProcessor {

    private final MappingContext mappingContext;

    public MappingContextAwareConfigurationUnitPostProcessor(final MappingContext mappingContext) {
        this.mappingContext = mappingContext;
    }

    @SuppressWarnings("unchecked")
    protected PersistentEntity getPersistentEntity(ConfigurationUnit configurationUnit) {
        return mappingContext.getPersistentEntity(configurationUnit.getDomainType());
    }

    @SuppressWarnings("unchecked")
    protected PersistentEntity getPersistentEntity(Class<?> domainType) {
        return mappingContext.getPersistentEntity(domainType);
    }
}