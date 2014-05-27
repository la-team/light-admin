package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.parsing.validation.ConfigurationUnitsValidator;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.support.ConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.support.DomainTypeMetadataAwareConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.support.EmptyConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.support.HierarchicalConfigurationPostProcessor;
import org.lightadmin.reporting.ProblemReporter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.support.Repositories;

import javax.persistence.EntityManager;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.apache.commons.lang3.ArrayUtils.toArray;
import static org.lightadmin.reporting.ProblemReporterFactory.failFastReporter;

@SuppressWarnings("unused")
public class GlobalAdministrationConfigurationFactoryBean extends AbstractFactoryBean<GlobalAdministrationConfiguration> implements InitializingBean {

    private DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;

    private Set<ConfigurationUnits> domainTypeConfigurationUnits;

    private EntityManager entityManager;

    private MappingContext<?, ?> mappingContext;

    private Repositories repositories;

    private ConfigurationUnitsValidator<ConfigurationUnits> configurationUnitsValidator;

    private ConfigurationUnitPostProcessor[] configurationUnitPostProcessors;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.configurationUnitPostProcessors = toArray(
                new EmptyConfigurationUnitPostProcessor(mappingContext),
                new DomainTypeMetadataAwareConfigurationUnitPostProcessor(mappingContext),
                new HierarchicalConfigurationPostProcessor());

        super.afterPropertiesSet();
    }

    @Override
    public Class<?> getObjectType() {
        return GlobalAdministrationConfiguration.class;
    }

    @Override
    protected GlobalAdministrationConfiguration createInstance() throws Exception {
        GlobalAdministrationConfiguration globalAdministrationConfiguration = new GlobalAdministrationConfiguration();

        ProblemReporter problemReporter = failFastReporter();

        for (ConfigurationUnits configurationUnits : domainTypeConfigurationUnits) {

            configurationUnitsValidator.validate(configurationUnits, problemReporter);

            configurationUnits = processConfigurationUnits(configurationUnits);

            DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = domainTypeAdministrationConfigurationFactory.createAdministrationConfiguration(configurationUnits);

            globalAdministrationConfiguration.registerDomainTypeConfiguration(domainTypeAdministrationConfiguration);

            registerAssociationDomainTypeConfigurations(domainTypeAdministrationConfiguration, globalAdministrationConfiguration);
        }

        return globalAdministrationConfiguration;
    }

    private void registerAssociationDomainTypeConfigurations(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, final GlobalAdministrationConfiguration globalAdministrationConfiguration) {
        PersistentEntity persistentEntity = domainTypeAdministrationConfiguration.getPersistentEntity();

        persistentEntity.doWithAssociations(new SimpleAssociationHandler() {
            @Override
            public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                Class<?> associationDomainType = association.getInverse().getActualType();

                if (isManagedEntity(associationDomainType) && !repositories.hasRepositoryFor(associationDomainType)) {
                    DomainTypeBasicConfiguration associationTypeConfiguration = domainTypeAdministrationConfigurationFactory.createNonManagedDomainTypeConfiguration(associationDomainType);
                    globalAdministrationConfiguration.registerNonDomainTypeConfiguration(associationTypeConfiguration);
                }
            }
        });
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

    private boolean isManagedEntity(Class type) {
        try {
            return entityManager.getMetamodel().entity(type) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void setDomainTypeConfigurationUnits(Set<ConfigurationUnits> domainTypeConfigurationUnits) {
        this.domainTypeConfigurationUnits = domainTypeConfigurationUnits;
    }

    public void setDomainTypeAdministrationConfigurationFactory(DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory) {
        this.domainTypeAdministrationConfigurationFactory = domainTypeAdministrationConfigurationFactory;
    }

    public void setConfigurationUnitsValidator(ConfigurationUnitsValidator<ConfigurationUnits> configurationUnitsValidator) {
        this.configurationUnitsValidator = configurationUnitsValidator;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setRepositories(Repositories repositories) {
        this.repositories = repositories;
    }

    public void setMappingContext(MappingContext<?, ?> mappingContext) {
        this.mappingContext = mappingContext;
    }
}