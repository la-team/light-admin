package org.lightadmin.core.config.rmi;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationClassDTO;

public interface GlobalConfigurationManagementService {

	void registerDomainTypeConfiguration( DomainConfigurationClassDTO domainConfigurationClassDTO );

}