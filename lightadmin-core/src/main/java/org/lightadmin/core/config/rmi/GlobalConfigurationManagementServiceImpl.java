package org.lightadmin.core.config.rmi;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationClassDTO;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class GlobalConfigurationManagementServiceImpl implements GlobalConfigurationManagementService {

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Override
	public void registerDomainTypeConfiguration( final DomainConfigurationClassDTO domainConfigurationClassDTO ) {
		globalAdministrationConfiguration.registerDomainTypeConfiguration( null );
	}
}