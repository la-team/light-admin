package org.lightadmin.core.config.management.jmx;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@ManagedResource( objectName = LightAdminConfigurationMonitoringServiceMBean.MBEAN_NAME,
				  description = "LightAdmin DomainType Administration Configuration Management Service" )
public class LightAdminConfigurationMonitoringServiceMBean {

	public static final String MBEAN_NAME = "org.lightadmin.mbeans:type=config,name=LightAdminConfigurationMonitoringServiceMBean";

	private final GlobalConfigurationManagementService globalConfigurationManagementService;

	public LightAdminConfigurationMonitoringServiceMBean( final GlobalConfigurationManagementService globalConfigurationManagementService ) {
		this.globalConfigurationManagementService = globalConfigurationManagementService;
	}

	@ManagedOperation( description = "List all registered Domain Type Configurations" )
	public Set<String> getDomainTypeAdministrationConfigurations() {
		final Set<String> result = newLinkedHashSet();
		for ( DomainTypeAdministrationConfiguration configuration : globalConfigurationManagementService.getRegisteredDomainTypeConfigurations() ) {
			result.add( configuration.getConfigurationName() );
		}
		return result;
	}

	@ManagedOperation( description = "List all registered Domain Types" )
	public Set<String> getDomainTypes() {
		final Set<String> result = newLinkedHashSet();
		for ( DomainTypeAdministrationConfiguration configuration : globalConfigurationManagementService.getRegisteredDomainTypeConfigurations() ) {
			result.add( configuration.getDomainTypeName() );
		}
		return result;
	}
}