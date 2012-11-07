package org.lightadmin.core.config.domain;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalAdministrationConfiguration {

	private final Map<Class<?>, DomainTypeAdministrationConfiguration> domainTypeConfigurations = new ConcurrentHashMap<Class<?>, DomainTypeAdministrationConfiguration>();

	public void registerDomainTypeConfiguration( DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration ) {
		domainTypeConfigurations.put( domainTypeAdministrationConfiguration.getDomainType(), domainTypeAdministrationConfiguration );
	}

	public void removeDomainTypeConfiguration( final Class<?> domainType ) {
		domainTypeConfigurations.remove( domainType );
	}

	public void removeAllDomainTypeAdministrationConfigurations() {
		domainTypeConfigurations.clear();
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