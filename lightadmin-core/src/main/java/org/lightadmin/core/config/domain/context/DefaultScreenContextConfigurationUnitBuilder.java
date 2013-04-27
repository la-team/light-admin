package org.lightadmin.core.config.domain.context;

import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

public class DefaultScreenContextConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<ScreenContextConfigurationUnit> implements ScreenContextConfigurationUnitBuilder {

	private String screenName;

	private String menuItemName;

	public DefaultScreenContextConfigurationUnitBuilder( final Class<?> domainType ) {
		super( domainType );

		this.screenName = this.menuItemName = domainType.getSimpleName();
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