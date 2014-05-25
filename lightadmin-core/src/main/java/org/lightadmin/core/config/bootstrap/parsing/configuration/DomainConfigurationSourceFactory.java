package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.support.ConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.support.DomainTypeMetadataAwareConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.support.EmptyConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.support.HierarchicalConfigurationPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.lang.String.format;
import static org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter.unitsFromAutowiredConfiguration;
import static org.lightadmin.core.util.DomainConfigurationUtils.isConfigurationCandidate;

public class DomainConfigurationSourceFactory {

    private final AutowireCapableBeanFactory beanFactory;
    private final MappingContext mappingContext;

    private final ConfigurationUnitPostProcessor[] configurationUnitPostProcessors;

    public DomainConfigurationSourceFactory(final MappingContext mappingContext, AutowireCapableBeanFactory beanFactory) {
        this(mappingContext, beanFactory,
                new EmptyConfigurationUnitPostProcessor(mappingContext),
                new DomainTypeMetadataAwareConfigurationUnitPostProcessor(mappingContext),
                new HierarchicalConfigurationPostProcessor());
    }

    public DomainConfigurationSourceFactory(final MappingContext mappingContext, AutowireCapableBeanFactory beanFactory, final ConfigurationUnitPostProcessor... configurationUnitPostProcessors) {
        this.beanFactory = beanFactory;
        this.mappingContext = mappingContext;
        this.configurationUnitPostProcessors = configurationUnitPostProcessors;
    }

    public DomainConfigurationSource createConfigurationSource(Object configurationMetadata) {
        Assert.notNull(configurationMetadata);

        if (isConfigurationCandidate(configurationMetadata)) {
            final Class configurationMetadataClass = (Class) configurationMetadata;

            return domainConfigurationUnitsSource(unitsFromAutowiredConfiguration(configurationMetadataClass, beanFactory));
        }

        if (configurationMetadata instanceof ConfigurationUnits) {
            ConfigurationUnits configurationUnits = (ConfigurationUnits) configurationMetadata;

            return domainConfigurationUnitsSource(configurationUnits);
        }

        throw new IllegalArgumentException(format("Configuration Metadata of type %s is not supported!", ClassUtils.getDescriptiveType(configurationMetadata)));
    }

    @SuppressWarnings("unchecked")
    DomainConfigurationSource domainConfigurationUnitsSource(final ConfigurationUnits configurationUnits) {
        final Class<?> domainType = configurationUnits.getDomainType();

        if (notPersistentEntityType(domainType)) {
            throw new IllegalArgumentException(format("Non-persistent type %s is not supported.", domainType.getSimpleName()));
        }

        PersistentEntity persistentEntity = mappingContext.getPersistentEntity(domainType);

        final ConfigurationUnits processedConfigurationUnits = processConfigurationUnits(configurationUnits);

        return new DomainConfigurationUnitsSource(persistentEntity, processedConfigurationUnits);
    }

    private ConfigurationUnits processConfigurationUnits(final ConfigurationUnits configurationUnits) {
        final Set<ConfigurationUnit> processedConfigurationUnits = newLinkedHashSet();
        for (ConfigurationUnit configurationUnit : configurationUnits) {
            ConfigurationUnit processedConfigurationUnit = configurationUnit;
            for (ConfigurationUnitPostProcessor configurationUnitPostProcessor : configurationUnitPostProcessors) {
                processedConfigurationUnit = configurationUnitPostProcessor.postProcess(processedConfigurationUnit, configurationUnits);
            }
            processedConfigurationUnits.add(processedConfigurationUnit);
        }
        return new ConfigurationUnits(configurationUnits.getConfigurationClassName(), configurationUnits.getDomainType(), processedConfigurationUnits);
    }

    @SuppressWarnings("unchecked")
    private boolean notPersistentEntityType(final Class<?> domainType) {
        return mappingContext.hasPersistentEntityFor(domainType);
    }
}
