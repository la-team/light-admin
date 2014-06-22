package org.lightadmin.core.config.context;

import org.lightadmin.api.config.management.rmi.DataManipulationService;
import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.management.rmi.DataManipulationServiceImpl;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LightAdminDomainConfiguration {

    @Bean
    @Autowired
    public GlobalConfigurationManagementService globalConfigurationManagementService(GlobalAdministrationConfiguration globalAdministrationConfiguration) {
        return new GlobalConfigurationManagementServiceImpl(globalAdministrationConfiguration);
    }

    @Bean
    @Autowired
    public DataManipulationService dataManipulationService(DataSource dataSource) {
        return new DataManipulationServiceImpl(dataSource);
    }
}