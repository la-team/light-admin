package org.lightadmin.core.config.domain;

import org.apache.commons.lang.StringUtils;

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
		return domainTypeConfigurations.get( domainType );
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