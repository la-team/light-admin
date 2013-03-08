package org.lightadmin.core.config.domain;

import org.apache.commons.lang.StringUtils;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class GlobalAdministrationConfiguration {

	private final DomainTypeAdministrationConfigurationFactory domainTypeConfigurationFactory;
	private final ConcurrentHashMap<Class<?>, DomainTypeAdministrationConfiguration> managedDomainTypeConfigurations = new ConcurrentHashMap<Class<?>, DomainTypeAdministrationConfiguration>();
	private final ConcurrentHashMap<Class<?>, DomainTypeBasicConfiguration> domainTypeConfigurations = new ConcurrentHashMap<Class<?>, DomainTypeBasicConfiguration>();

	public GlobalAdministrationConfiguration( DomainTypeAdministrationConfigurationFactory domainTypeConfigurationFactory ) {
		this.domainTypeConfigurationFactory = domainTypeConfigurationFactory;
	}

	public void registerDomainTypeConfiguration( DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration ) {
		managedDomainTypeConfigurations.put( domainTypeAdministrationConfiguration.getDomainType(), domainTypeAdministrationConfiguration );
		registerAssociationDomainTypeConfigurations( domainTypeAdministrationConfiguration );
	}

	private void registerAssociationDomainTypeConfigurations( DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration ) {

		domainTypeConfigurations.put( domainTypeAdministrationConfiguration.getDomainType(), domainTypeAdministrationConfiguration );

		DomainTypeEntityMetadata<DomainTypeAttributeMetadata> entityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();
		for ( DomainTypeAttributeMetadata attrMetadata : entityMetadata.getAttributes() ) {
			if ( !attrMetadata.isAssociation() ) {
				continue;
			}
			Class<?> associationDomainType = attrMetadata.getAssociationDomainType();
			DomainTypeBasicConfiguration associationTypeConfiguration = domainTypeConfigurationFactory.createNonManagedDomainTypeConfiguration( associationDomainType );
			domainTypeConfigurations.putIfAbsent( associationDomainType, associationTypeConfiguration );
		}
	}

	public void removeDomainTypeConfiguration( final Class<?> domainType ) {
		managedDomainTypeConfigurations.remove( domainType );
	}

	public void removeAllDomainTypeAdministrationConfigurations() {
		managedDomainTypeConfigurations.clear();

		domainTypeConfigurations.clear();
	}

	public Map<Class<?>, DomainTypeAdministrationConfiguration> getManagedDomainTypeConfigurations() {
		return managedDomainTypeConfigurations;
	}

	public Map<Class<?>, DomainTypeBasicConfiguration> getDomainTypeConfigurations() {
		return domainTypeConfigurations;
	}

	public Collection<DomainTypeAdministrationConfiguration> getDomainTypeConfigurationsValues() {
		return managedDomainTypeConfigurations.values();
	}

	public DomainTypeAdministrationConfiguration forManagedDomainType( Class<?> domainType ) {
		return managedDomainTypeConfigurations.get( domainType );
	}

	public DomainTypeBasicConfiguration forDomainType( Class<?> domainType ) {
		return domainTypeConfigurations.get( domainType );
	}

	public DomainTypeAdministrationConfiguration forEntityName( String entityName ) {
		for ( DomainTypeAdministrationConfiguration configuration : managedDomainTypeConfigurations.values() ) {
			if ( StringUtils.equalsIgnoreCase( entityName, configuration.getDomainTypeName() ) ) {
				return configuration;
			}
		}
		throw new RuntimeException( "Undefined entity name. Please check your configuration." );
	}
}