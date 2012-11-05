package org.lightadmin.core.config.mbeans;

import org.lightadmin.core.config.bootstrap.DomainTypeAdministrationConfigurationReader;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.reporting.ProblemReporterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@ManagedResource( objectName = GlobalAdministrationConfigurationManagementService.MBEAN_NAME,
				  description = "LightAdmin DomainType Administration Configuration Management Service" )
public class GlobalAdministrationConfigurationManagementService {

	public static final String MBEAN_NAME = "org.lightadmin.mbeans:type=config,name=GlobalAdministrationConfigurationManagementService";

	@Autowired
	private GlobalAdministrationConfiguration globalAdministrationConfiguration;

	@Autowired
	private DomainTypeAdministrationConfigurationReader<Class> domainTypeAdministrationConfigurationReader;

	@ManagedOperation( description = "Registers new DSL Configuration" )
	@ManagedOperationParameters( {@ManagedOperationParameter( description = "The configuration class", name = "configurationClass" )} )

	public void registerDomainTypeConfiguration( Class configurationClass ) {
		final Set<DomainTypeAdministrationConfiguration> domainTypeAdministrationConfigurations = domainTypeAdministrationConfigurationReader.loadDomainTypeConfiguration( newHashSet( configurationClass ), ProblemReporterFactory.failFastReporter() );

		globalAdministrationConfiguration.registerDomainTypeConfigurations( domainTypeAdministrationConfigurations );
	}
}