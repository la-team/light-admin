package org.lightadmin.core.web.util;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;

public final class ApplicationUrlResolver {

	private ApplicationUrlResolver() {
	}

	public static String domainBaseUrl( DomainTypeAdministrationConfiguration configuration ) {
		return "/domain/" + configuration.getDomainTypeName();
	}

	public static String domainRestBaseUrl( DomainTypeAdministrationConfiguration configuration ) {
		return "/rest/" + configuration.getDomainTypeName();
	}

	public static String domainRestEntityBaseUrl( DomainTypeAdministrationConfiguration configuration, Object id ) {
		return domainRestBaseUrl( configuration ) + "/" + id;
	}

	public static String domainRestScopeBaseUrl( DomainTypeAdministrationConfiguration configuration ) {
		return "/rest/" + configuration.getDomainTypeName() + "/scope";
	}
}