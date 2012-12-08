package org.lightadmin.core.config.bootstrap.parsing.configuration;

public enum DomainConfigurationUnitType {

	SCREEN_CONTEXT( "screenContext" ),
	CONFIGURATION( "configuration" ),
	LIST_VIEW( "listView" ),
	SCOPES( "scopes" ),
	FILTERS( "filters" ),
	SHOW_VIEW( "showView" ),
	FORM_VIEW( "formView" ),
	QUICK_VIEW( "quickView" );

	private final String name;

	private DomainConfigurationUnitType( final String name ) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static DomainConfigurationUnitType forName( String name ) {
		for ( DomainConfigurationUnitType domainConfigurationUnitType : values() ) {
			if ( domainConfigurationUnitType.getName().equals( name ) ) {
				return domainConfigurationUnitType;
			}
		}
		throw new IllegalArgumentException( String.format( "Configuration Unit for name %s not defined!", name ) );
	}

	@Override
	public String toString() {
		return String.format( "Domain Configuration Unit: %s", getName() );
	}
}