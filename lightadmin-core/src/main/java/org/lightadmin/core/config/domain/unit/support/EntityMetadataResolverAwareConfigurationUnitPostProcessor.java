package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;

public abstract class EntityMetadataResolverAwareConfigurationUnitPostProcessor implements ConfigurationUnitPostProcessor {

    private final MappingContext mappingContext;

    public EntityMetadataResolverAwareConfigurationUnitPostProcessor(final MappingContext mappingContext) {
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