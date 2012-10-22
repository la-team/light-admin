package org.lightadmin.core.config.beans.parsing;

public enum DslConfigurationUnit {

	SCREEN_CONTEXT("screenContext"),
	CONFIGURATION( "configuration" ),
	LIST_VIEW( "listView" ),
	SCOPES( "scopes" ),
	FILTERS( "filters" );

	private String name;

	private DslConfigurationUnit( final String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format( "DSL Configuration Unit: %s", getName() );
	}
}