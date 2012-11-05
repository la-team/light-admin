package org.lightadmin.core.config.beans.parsing;

import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationUnit;
import org.lightadmin.core.reporting.Problem;

public class DomainConfigurationProblem extends Problem {

	private final DomainConfigurationSource domainConfiguration;

	private DomainConfigurationUnit configurationUnit;

	public DomainConfigurationProblem( final DomainConfigurationSource domainConfiguration, final String message ) {
		super( message );
		this.domainConfiguration = domainConfiguration;
	}

	public DomainConfigurationProblem( final DomainConfigurationSource domainConfiguration, DomainConfigurationUnit configurationUnit, final String message ) {
		this( domainConfiguration, message );
		this.configurationUnit = configurationUnit;
	}

	@Override
	public String getMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append( "Domain Configuration " );
		stringBuilder.append( "\"" ).append( domainConfiguration.getConfigurationName() ).append( "\"" ).append( ": " );
		if ( configurationUnit != null ) {
			stringBuilder.append( "Unit " ).append( "\"" ).append( configurationUnit.getName() ).append( "\"" ).append( ": " );
		}
		stringBuilder.append( super.getMessage() );

		return stringBuilder.toString();
	}

	@Override
	public String toString() {
		return getMessage();
	}

	public DomainConfigurationSource getDomainConfiguration() {
		return domainConfiguration;
	}

	public DomainConfigurationUnit getConfigurationUnit() {
		return configurationUnit;
	}
}