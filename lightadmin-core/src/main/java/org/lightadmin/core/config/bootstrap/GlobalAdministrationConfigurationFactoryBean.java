package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimpleAssociationHandler;

import java.util.Set;

public class GlobalAdministrationConfigurationFactoryBean extends AbstractFactoryBean<GlobalAdministrationConfiguration> implements InitializingBean {

    private DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;

    private Set<ConfigurationUnits> domainTypeConfigurationUnits;

    @Override
    public Class<?> getObjectType() {
        return GlobalAdministrationConfiguration.class;
    }

    @Override
    protected GlobalAdministrationConfiguration createInstance() throws Exception {
        GlobalAdministrationConfiguration globalAdministrationConfiguration = new GlobalAdministrationConfiguration();

        for (ConfigurationUnits configurationUnits : domainTypeConfigurationUnits) {
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
                Class<?> associationDomainType = association.getInverse().getComponentType();
                DomainTypeBasicConfiguration associationTypeConfiguration = domainTypeAdministrationConfigurationFactory.createNonManagedDomainTypeConfiguration(associationDomainType);

                globalAdministrationConfiguration.registerNonDomainTypeConfiguration(associationTypeConfiguration);
            }
        });
    }

    public void setDomainTypeConfigurationUnits(Set<ConfigurationUnits> domainTypeConfigurationUnits) {
        this.domainTypeConfigurationUnits = domainTypeConfigurationUnits;
    }

    public void setDomainTypeAdministrationConfigurationFactory(DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory) {
        this.domainTypeAdministrationConfigurationFactory = domainTypeAdministrationConfigurationFactory;
    }
}