package org.lightadmin.core.config.domain;

import org.apache.commons.lang.StringUtils;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings( "unchecked" )
public class GlobalAdministrationConfiguration {

	private final DomainTypeAdministrationConfigurationFactory domainTypeConfigurationFactory;
	private final ConcurrentHashMap<Class<?>, DomainTypeAdministrationConfiguration> domainTypeConfigurations = new ConcurrentHashMap<Class<?>, DomainTypeAdministrationConfiguration>();
	private final ConcurrentHashMap<Class<?>, DomainTypeBasicConfiguration> associationDomainTypeConfigurations = new ConcurrentHashMap<Class<?>, DomainTypeBasicConfiguration>();

	public GlobalAdministrationConfiguration(DomainTypeAdministrationConfigurationFactory domainTypeConfigurationFactory) {
		this.domainTypeConfigurationFactory = domainTypeConfigurationFactory;
	}

	public void registerDomainTypeConfiguration( DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration ) {
		domainTypeConfigurations.put( domainTypeAdministrationConfiguration.getDomainType(), domainTypeAdministrationConfiguration );
		registerAssociationDomainTypeConfigurations(domainTypeAdministrationConfiguration);
	}

	private void registerAssociationDomainTypeConfigurations(DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration) {

		associationDomainTypeConfigurations.put(domainTypeAdministrationConfiguration.getDomainType(), domainTypeAdministrationConfiguration);

		DomainTypeEntityMetadata<DomainTypeAttributeMetadata> entityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();
		for (DomainTypeAttributeMetadata attrMetadata : entityMetadata.getAttributes()) {
			if (!attrMetadata.isAssociation()) {
				continue;
			}
			Class<?> associationDomainType = associationDomainType(attrMetadata);
			DomainTypeBasicConfiguration associationTypeConfiguration = domainTypeConfigurationFactory.createNonmanagedDomainTypeConfiguration(associationDomainType);
			associationDomainTypeConfigurations.putIfAbsent(associationDomainType, associationTypeConfiguration);
		}
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

	public Collection<DomainTypeAdministrationConfiguration> getDomainTypeConfigurationsValues() {
		return domainTypeConfigurations.values();
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

	public DomainTypeBasicConfiguration forAssociation( DomainTypeAttributeMetadata attrMetadata ) {
		Class<?> associationDomainType = associationDomainType(attrMetadata);
		return associationDomainTypeConfigurations.get(associationDomainType);
	}

	public DomainTypeBasicConfiguration forAssociation( Class<?> associationDomainType ) {
		return associationDomainTypeConfigurations.get(associationDomainType);
	}

	private Class<?> associationDomainType(DomainTypeAttributeMetadata attrMetadata) {
		if (attrMetadata.getAttribute().isCollection()) {
			return attrMetadata.getElementType();
		} else {
			return attrMetadata.getType();
		}
	}
}
