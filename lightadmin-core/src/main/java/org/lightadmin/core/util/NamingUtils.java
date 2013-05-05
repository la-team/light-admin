package org.lightadmin.core.util;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public class NamingUtils {

	@SuppressWarnings("unchecked")
	public static String entityName( DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, Object entity ) {
		final EntityNameExtractor nameExtractor = domainTypeAdministrationConfiguration.getEntityConfiguration().getNameExtractor();

		return nameExtractor.apply( entity );
	}

	public static String entityId( DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration, Object entity ) {
		final DomainTypeEntityMetadata domainTypeEntityMetadata = domainTypeAdministrationConfiguration.getDomainTypeEntityMetadata();

		return String.valueOf( domainTypeEntityMetadata.getIdAttribute().getValue( entity ) );
	}
}