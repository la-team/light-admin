package org.lightadmin.core.config.bootstrap.parsing;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;

import static java.lang.String.format;

@SuppressWarnings("unused")
public class InvalidPropertyConfigurationProblem extends DomainConfigurationProblem {

	private final String propertyName;

	public InvalidPropertyConfigurationProblem( String propertyName, final DomainConfigurationSource domainConfiguration, DomainConfigurationUnitType configurationUnitType ) {
		super( domainConfiguration, configurationUnitType, format( "Invalid property/path '%s' defined!", propertyName ) );
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}
}