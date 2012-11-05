package org.lightadmin.core.config.rmi;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceDTO;

public interface GlobalConfigurationManagementService {

	void registerDomainTypeConfiguration( DomainConfigurationSourceDTO domainConfigurationSourceDTO );

}