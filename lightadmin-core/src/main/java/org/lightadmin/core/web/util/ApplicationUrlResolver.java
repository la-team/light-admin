package org.lightadmin.core.web.util;

public final class ApplicationUrlResolver {

	private ApplicationUrlResolver() {
	}

	public static String domainBaseUrl( String domainTypeName ) {
		return "/domain/" + domainTypeName;
	}

	public static String domainRestBaseUrl( String domainTypeName ) {
		return "/rest/" + domainTypeName;
	}

	public static String domainRestEntityBaseUrl( String domainTypeName, Object id ) {
		return "/rest/" + domainTypeName  + "/" + id;
	}

	public static String domainRestScopeBaseUrl( String domainTypeName ) {
		return domainRestBaseUrl( domainTypeName ) + "/scope";
	}

	public static String domainRestFilterBaseUrl( String domainTypeName ) {
		return domainRestBaseUrl( domainTypeName ) + "/filter";
	}
}