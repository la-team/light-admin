package org.lightadmin.core.config.bootstrap.parsing.configuration;

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

	public static DomainConfigurationUnit forName( String name ) {
		for ( DomainConfigurationUnit domainConfigurationUnit : values() ) {
			if ( domainConfigurationUnit.getName().equals( name ) ) {
				return domainConfigurationUnit;
			}
		}
		throw new IllegalArgumentException( String.format( "Configuration Unit for name %s not defined!", name ) );
	}

	@Override
	public String toString() {
		return String.format( "Domain Configuration Unit: %s", getName() );
	}
}