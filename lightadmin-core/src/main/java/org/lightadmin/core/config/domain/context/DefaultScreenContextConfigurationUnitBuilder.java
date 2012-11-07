package org.lightadmin.core.config.domain.context;

import org.lightadmin.core.config.domain.support.DomainTypeConfigurationUnitBuilder;

public class DefaultScreenContextConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<ScreenContextConfigurationUnit> implements ScreenContextConfigurationUnitBuilder {

	private String screenName = "Undefined";

	private String menuItemName = "Undefined";

	public DefaultScreenContextConfigurationUnitBuilder( final Class<?> domainType ) {
		super( domainType );
	}

	@Override
	public ScreenContextConfigurationUnitBuilder screenName( final String screenName ) {
		this.screenName = screenName;
		return this;
	}

	@Override
	public ScreenContextConfigurationUnitBuilder menuName( final String menuItemName ) {
		this.menuItemName = menuItemName;
		return this;
	}

	@Override
	public ScreenContextConfigurationUnit build() {
		return new DefaultScreenContextConfigurationUnit( getDomainType(), screenName, menuItemName );
	}
}