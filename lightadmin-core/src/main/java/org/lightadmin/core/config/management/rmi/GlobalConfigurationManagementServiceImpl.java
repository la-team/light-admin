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
package org.lightadmin.core.config.management.rmi;

import com.google.common.collect.Sets;
import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.bootstrap.GlobalAdministrationConfigurationFactoryBean;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.util.Collection;

public class GlobalConfigurationManagementServiceImpl implements GlobalConfigurationManagementService, ApplicationContextAware {

    private final GlobalAdministrationConfiguration globalAdministrationConfiguration;
    private final RepositoryRestConfiguration repositoryRestConfiguration;

    private ApplicationContext applicationContext;

    public GlobalConfigurationManagementServiceImpl(GlobalAdministrationConfiguration globalAdministrationConfiguration, RepositoryRestConfiguration repositoryRestConfiguration) {
        this.globalAdministrationConfiguration = globalAdministrationConfiguration;
        this.repositoryRestConfiguration = repositoryRestConfiguration;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerDomainTypeConfiguration(ConfigurationUnits... configurationUnits) {
        GlobalAdministrationConfiguration administrationConfiguration = newGlobalAdministrationConfiguration(configurationUnits);

        for (Class<?> managedType : administrationConfiguration.getManagedDomainTypes()) {
            globalAdministrationConfiguration.registerDomainTypeConfiguration(administrationConfiguration.forManagedDomainType(managedType));
        }

        for (Class<?> nonManagedType : administrationConfiguration.getNonManagedDomainTypes()) {
            globalAdministrationConfiguration.registerNonDomainTypeConfiguration(administrationConfiguration.forDomainType(nonManagedType));
        }

        repositoryRestConfiguration.exposeIdsFor(administrationConfiguration.getAllDomainTypesAsArray());
    }

    @Override
    public void removeDomainTypeAdministrationConfiguration(Class<?> domainType) {
        globalAdministrationConfiguration.removeDomainTypeConfiguration(domainType);
    }

    @Override
    public void removeAllDomainTypeAdministrationConfigurations() {
        globalAdministrationConfiguration.removeAllDomainTypeAdministrationConfigurations();
    }

    @Override
    public Collection<DomainTypeAdministrationConfiguration> getRegisteredDomainTypeConfigurations() {
        return globalAdministrationConfiguration.getDomainTypeConfigurationsValues();
    }

    @Override
    public DomainTypeAdministrationConfiguration getRegisteredDomainTypeConfiguration(final Class<?> domainType) {
        return globalAdministrationConfiguration.forManagedDomainType(domainType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private GlobalAdministrationConfiguration newGlobalAdministrationConfiguration(ConfigurationUnits... configurationUnits) {
        try {
            return newGlobalAdministrationConfigurationFactoryBeanFor(configurationUnits).getObject();
        } catch (Exception e) {
            throw (RuntimeException) e;
        }
    }

    private GlobalAdministrationConfigurationFactoryBean newGlobalAdministrationConfigurationFactoryBeanFor(ConfigurationUnits... configurationUnits) throws Exception {
        GlobalAdministrationConfigurationFactoryBean factoryBean = GlobalAdministrationConfigurationFactoryBean.newInstance(globalAdministrationConfigurationFactoryBean());
        factoryBean.setDomainTypeConfigurationUnits(Sets.newHashSet(configurationUnits));
        return factoryBean;
    }

    private GlobalAdministrationConfigurationFactoryBean globalAdministrationConfigurationFactoryBean() {
        return applicationContext.getBean(GlobalAdministrationConfigurationFactoryBean.class);
    }
}