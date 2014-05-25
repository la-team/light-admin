package org.lightadmin.core.config.management.rmi;

import org.lightadmin.api.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidator;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidatorFactory;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.reporting.ProblemReporterFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class GlobalConfigurationManagementServiceImpl implements GlobalConfigurationManagementService {

    @Autowired
    private GlobalAdministrationConfiguration globalAdministrationConfiguration;

    @Autowired
    private DomainConfigurationSourceFactory domainConfigurationSourceFactory;

    @Autowired
    private DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory;

    @Autowired
    private DomainConfigurationSourceValidatorFactory configurationSourceValidatorFactory;


    @Override
    @SuppressWarnings("unchecked")
    public void registerDomainTypeConfiguration(final ConfigurationUnits configurationUnits) {
        final DomainConfigurationSource configurationSource = domainConfigurationSourceFactory.createConfigurationSource(configurationUnits);

        final DomainConfigurationSourceValidator configurationSourceValidator = configurationSourceValidatorFactory.getValidator();

        configurationSourceValidator.validate(configurationSource, ProblemReporterFactory.failFastReporter());

        final DomainTypeAdministrationConfiguration administrationConfiguration = domainTypeAdministrationConfigurationFactory.createAdministrationConfiguration(null);

        globalAdministrationConfiguration.registerDomainTypeConfiguration(administrationConfiguration);
    }

    @Override
    public void removeDomainTypeAdministrationConfiguration(final Class<?> domainType) {
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
}
