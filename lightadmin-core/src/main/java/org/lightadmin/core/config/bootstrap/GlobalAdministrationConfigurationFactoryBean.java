package org.lightadmin.core.config.bootstrap;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.data.repository.support.Repositories;

import java.util.Set;

public class GlobalAdministrationConfigurationFactoryBean extends AbstractFactoryBean<GlobalAdministrationConfiguration> {

    private Set<ConfigurationUnits> configurationUnits;

    private Repositories repositories;

    @Override
    public Class<?> getObjectType() {
        return GlobalAdministrationConfiguration.class;
    }

    @Override
    protected GlobalAdministrationConfiguration createInstance() throws Exception {
        GlobalAdministrationConfiguration globalAdministrationConfiguration = new GlobalAdministrationConfiguration(null);
        return globalAdministrationConfiguration;
    }

    public void setConfigurationUnits(Set<ConfigurationUnits> configurationUnits) {
        this.configurationUnits = configurationUnits;
    }

    public void setRepositories(Repositories repositories) {
        this.repositories = repositories;
    }
}