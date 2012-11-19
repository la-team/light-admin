package org.lightadmin.core.config.management.rmi;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;

import java.util.Collection;

public interface GlobalConfigurationManagementService {

	void registerDomainTypeConfiguration( ConfigurationUnits configurationUnits );

	void removeDomainTypeAdministrationConfiguration( Class<?> domainType );

	void removeAllDomainTypeAdministrationConfigurations();

	Collection<DomainTypeAdministrationConfiguration> getRegisteredDomainTypeConfigurations();

	DomainTypeAdministrationConfiguration getRegisteredDomainTypeConfiguration( Class<?> domainType );
}