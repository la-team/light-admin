package org.lightadmin.core.config.management.rmi;

import com.google.common.collect.Sets;
import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.bootstrap.GlobalAdministrationConfigurationFactoryBean;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

public class GlobalConfigurationManagementServiceImpl implements GlobalConfigurationManagementService, ApplicationContextAware {

    private GlobalAdministrationConfiguration globalAdministrationConfiguration;

    private ApplicationContext applicationContext;

    public GlobalConfigurationManagementServiceImpl(GlobalAdministrationConfiguration globalAdministrationConfiguration) {
        this.globalAdministrationConfiguration = globalAdministrationConfiguration;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerDomainTypeConfiguration(ConfigurationUnits configurationUnits) {
        GlobalAdministrationConfiguration administrationConfiguration = newGlobalAdministrationConfiguration(configurationUnits);

        for (Class<?> managedType : administrationConfiguration.getManagedDomainTypes()) {
            globalAdministrationConfiguration.registerDomainTypeConfiguration(administrationConfiguration.forManagedDomainType(managedType));
        }

        for (Class<?> nonManagedType : administrationConfiguration.getNonManagedDomainTypes()) {
            globalAdministrationConfiguration.registerNonDomainTypeConfiguration(administrationConfiguration.forDomainType(nonManagedType));
        }
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

    private GlobalAdministrationConfiguration newGlobalAdministrationConfiguration(ConfigurationUnits configurationUnits) {
        try {
            return newGlobalAdministrationConfigurationFactoryBeanFor(configurationUnits).getObject();
        } catch (Exception e) {
            throw (RuntimeException) e;
        }
    }

    private GlobalAdministrationConfigurationFactoryBean newGlobalAdministrationConfigurationFactoryBeanFor(ConfigurationUnits configurationUnits) throws Exception {
        GlobalAdministrationConfigurationFactoryBean factoryBean = GlobalAdministrationConfigurationFactoryBean.newInstance(globalAdministrationConfigurationFactoryBean());
        factoryBean.setDomainTypeConfigurationUnits(Sets.<ConfigurationUnits>newHashSet(configurationUnits));
        return factoryBean;
    }

    private GlobalAdministrationConfigurationFactoryBean globalAdministrationConfigurationFactoryBean() {
        return applicationContext.getBean(GlobalAdministrationConfigurationFactoryBean.class);
    }
}