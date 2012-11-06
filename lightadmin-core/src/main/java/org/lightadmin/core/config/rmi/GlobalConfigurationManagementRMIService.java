package org.lightadmin.core.config.rmi;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationClassDTO;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigFactory;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class GlobalConfigurationManagementRMIService implements GlobalConfigurationManagementService {

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Autowired
	private DomainConfigurationSourceFactory domainConfigurationSourceFactory;

	@Autowired
	private DomainTypeAdministrationConfigFactory domainTypeAdministrationConfigFactory;

	@Override
	public void registerDomainTypeConfiguration( final DomainConfigurationClassDTO domainConfigurationClassDTO ) {
		final DomainConfigurationSource configurationSource = domainConfigurationSourceFactory.createConfigurationSource( domainConfigurationClassDTO );

		final DomainTypeAdministrationConfiguration administrationConfiguration = domainTypeAdministrationConfigFactory.createAdministrationConfiguration( configurationSource );

		globalAdministrationConfiguration.registerDomainTypeConfiguration( administrationConfiguration );
	}

	@Override
	public void removeDomainTypeAdministrationConfiguration( final Class<?> domainType ) {
		globalAdministrationConfiguration.removeDomainTypeConfiguration( domainType );
	}
}