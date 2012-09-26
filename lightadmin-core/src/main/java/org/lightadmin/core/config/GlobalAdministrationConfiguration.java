package org.lightadmin.core.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.Map;

public class GlobalAdministrationConfiguration {

	private Map<Class<?>, DomainTypeAdministrationConfiguration> domainTypeConfigurations;

	public void setDomainTypeConfigurations( final Map<Class<?>, DomainTypeAdministrationConfiguration> domainTypeConfigurations ) {
		this.domainTypeConfigurations = domainTypeConfigurations;
	}

	public Map<Class<?>, DomainTypeAdministrationConfiguration> getDomainTypeConfigurations() {
		return domainTypeConfigurations;
	}

	public DomainTypeAdministrationConfiguration forDomainType( Class<?> domainType ) {
		final DomainTypeAdministrationConfiguration configuration = domainTypeConfigurations.get( domainType );
		Assert.notNull( configuration, "Undefined entity name. Please check your configuration." );
		return configuration;
	}

	public DomainTypeAdministrationConfiguration forEntityName( String entityName ) {
		for ( DomainTypeAdministrationConfiguration configuration : domainTypeConfigurations.values() ) {
			if ( StringUtils.equalsIgnoreCase( entityName, configuration.getDomainTypeName() ) ) {
				return configuration;
			}
		}
		throw new RuntimeException( "Undefined entity name. Please check your configuration." );
	}
}