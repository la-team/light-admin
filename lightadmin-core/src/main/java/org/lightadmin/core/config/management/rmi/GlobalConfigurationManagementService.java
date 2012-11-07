package org.lightadmin.core.config.management.rmi;

import org.lightadmin.core.config.domain.support.ConfigurationUnits;

public interface GlobalConfigurationManagementService {

	void registerDomainTypeConfiguration( ConfigurationUnits configurationUnits );

	void removeDomainTypeAdministrationConfiguration( Class<?> domainType );
}