package org.lightadmin.core.config.management.jmx;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

@ManagedResource( objectName = LightAdminConfigurationMonitoringServiceMBean.MBEAN_NAME,
				  description = "LightAdmin DomainType Administration Configuration Management Service" )
public class LightAdminConfigurationMonitoringServiceMBean {

	public static final String MBEAN_NAME = "org.lightadmin.mbeans:type=config,name=LightAdminConfigurationMonitoringServiceMBean";

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@ManagedOperation( description = "List all registered Domain Type Configurations" )
	public Set<String> getDomainTypeAdministrationConfigurations() {
		final Set<String> result = newLinkedHashSet();
		for ( DomainTypeAdministrationConfiguration configuration : domainTypeConfigurations() ) {
			result.add( configuration.getConfigurationName() );
		}
		return result;
	}

	@ManagedOperation( description = "List all registered Domain Types" )
	public Set<String> getDomainTypes() {
		final Set<String> result = newLinkedHashSet();
		for ( DomainTypeAdministrationConfiguration configuration : domainTypeConfigurations() ) {
			result.add( configuration.getDomainTypeName() );
		}
		return result;
	}

	private Collection<DomainTypeAdministrationConfiguration> domainTypeConfigurations() {
		return globalAdministrationConfiguration.getDomainTypeConfigurations().values();
	}
}