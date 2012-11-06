package org.lightadmin.core.config.management.rmi;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationClassDTO;

import java.util.Set;

public interface GlobalConfigurationManagementService {

	void registerDomainTypeConfiguration( DomainConfigurationClassDTO domainConfigurationClassDTO );

	void registerDomainTypeConfigurations( Set<DomainConfigurationClassDTO> domainConfigurationClassDTO );

	void removeDomainTypeAdministrationConfiguration( Class<?> domainType );
}