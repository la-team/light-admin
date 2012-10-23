package org.lightadmin.core.config.beans.parsing;

import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationUnit;

@SuppressWarnings( "unused" )
public class InvalidPropertyConfigurationProblem extends DomainConfigurationProblem {

	private final String propertyName;

	public InvalidPropertyConfigurationProblem( String propertyName, final DomainConfigurationInterface domainConfiguration, DomainConfigurationUnit configurationUnit ) {
		super( domainConfiguration, configurationUnit, String.format( "Unexisting property/path '%s' defined!", propertyName ) );
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}
}