package org.lightadmin.core.config.bootstrap.parsing;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;

@SuppressWarnings( "unused" )
public class InvalidPropertyConfigurationProblem extends DomainConfigurationProblem {

	private final String propertyName;

	public InvalidPropertyConfigurationProblem( String propertyName, final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType ) {
		super( domainConfiguration, configurationUnitType, String.format( "Unexisting property/path '%s' defined!", propertyName ) );
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}
}