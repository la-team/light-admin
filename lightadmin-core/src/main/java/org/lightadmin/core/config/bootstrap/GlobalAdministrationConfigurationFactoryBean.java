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
package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationProblem;
import org.lightadmin.core.config.bootstrap.parsing.validation.ConfigurationUnitsValidator;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.processor.ConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.processor.EmptyConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.processor.HierarchicalConfigurationPostProcessor;
import org.lightadmin.core.config.domain.unit.processor.VisitableConfigurationUnitPostProcessor;
import org.lightadmin.core.reporting.DefaultProblemReporter;
import org.lightadmin.core.reporting.ProblemReporter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.support.Repositories;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static java.lang.String.format;
import static org.apache.commons.lang3.ArrayUtils.toArray;

@SuppressWarnings("unused")
public class GlobalAdministrationConfigurationFactoryBean extends AbstractFactoryBean<GlobalAdministrationConfiguration> implements InitializingBean {

    private ProblemReporter problemReporter = DefaultProblemReporter.INSTANCE;

    private DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;

    private Set<ConfigurationUnits> domainTypeConfigurationUnits;

    private MappingContext<?, ?> mappingContext;

    private Repositories repositories;

    private ConfigurationUnitsValidator<ConfigurationUnits> configurationUnitsValidator;

    private ConfigurationUnitPostProcessor[] configurationUnitPostProcessors;

    public static GlobalAdministrationConfigurationFactoryBean newInstance(GlobalAdministrationConfigurationFactoryBean factoryBeanEtalon) throws Exception {
        GlobalAdministrationConfigurationFactoryBean result = new GlobalAdministrationConfigurationFactoryBean();
        result.setSingleton(false);
        result.setRepositories(factoryBeanEtalon.getRepositories());
        result.setConfigurationUnitsValidator(factoryBeanEtalon.getConfigurationUnitsValidator());
        result.setDomainTypeAdministrationConfigurationFactory(factoryBeanEtalon.getDomainTypeAdministrationConfigurationFactory());
        result.setMappingContext(factoryBeanEtalon.getMappingContext());
        result.setBeanFactory(factoryBeanEtalon.getBeanFactory());
        result.afterPropertiesSet();
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.configurationUnitPostProcessors = toArray(
                new EmptyConfigurationUnitPostProcessor(mappingContext),
                new VisitableConfigurationUnitPostProcessor(mappingContext),
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

        for (ConfigurationUnits configurationUnits : domainTypeConfigurationUnits) {
            if (nonPersistentEntityType(configurationUnits.getDomainType())) {
                problemReporter.handle(new DomainConfigurationProblem(configurationUnits, format("Administration of non-persistent type %s is not supported.", configurationUnits.getDomainType().getSimpleName())));
                continue;
            }

            configurationUnits = preprocessConfigurationUnits(configurationUnits);

            configurationUnitsValidator.validate(configurationUnits, problemReporter);

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

                if (!isManagedEntity(associationDomainType) && repositories.hasRepositoryFor(associationDomainType)) {
                    DomainTypeBasicConfiguration associationTypeConfiguration = domainTypeAdministrationConfigurationFactory.createNonManagedDomainTypeConfiguration(associationDomainType);
                    globalAdministrationConfiguration.registerNonDomainTypeConfiguration(associationTypeConfiguration);
                }
            }
        });
    }

    private ConfigurationUnits preprocessConfigurationUnits(final ConfigurationUnits configurationUnits) {
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
        for (ConfigurationUnits domainTypeConfigurationUnit : domainTypeConfigurationUnits) {
            if (domainTypeConfigurationUnit.getDomainType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean nonPersistentEntityType(final Class<?> domainType) {
        return !mappingContext.hasPersistentEntityFor(domainType);
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

    public void setRepositories(Repositories repositories) {
        this.repositories = repositories;
    }

    public void setMappingContext(MappingContext<?, ?> mappingContext) {
        this.mappingContext = mappingContext;
    }

    public DomainTypeAdministrationConfigurationFactory getDomainTypeAdministrationConfigurationFactory() {
        return domainTypeAdministrationConfigurationFactory;
    }

    public Set<ConfigurationUnits> getDomainTypeConfigurationUnits() {
        return domainTypeConfigurationUnits;
    }

    public MappingContext<?, ?> getMappingContext() {
        return mappingContext;
    }

    public Repositories getRepositories() {
        return repositories;
    }

    public ConfigurationUnitsValidator<ConfigurationUnits> getConfigurationUnitsValidator() {
        return configurationUnitsValidator;
    }

    @Override
    public BeanFactory getBeanFactory() {
        return super.getBeanFactory();
    }
}