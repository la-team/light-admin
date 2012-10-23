package org.lightadmin.core.config.beans.parsing.configuration;

public enum DomainConfigurationUnit {

	SCREEN_CONTEXT( "screenContext" ),
	CONFIGURATION( "configuration" ),
	LIST_VIEW( "listView" ),
	SCOPES( "scopes" ),
	FILTERS( "filters" );

	private String name;

	private DomainConfigurationUnit( final String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format( "Domain Configuration Unit: %s", getName() );
	}
}