package org.lightadmin.core.config.domain.context;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.support.DomainTypeConfigurationUnit;

public class DefaultScreenContextConfigurationUnit extends DomainTypeConfigurationUnit implements ScreenContextConfigurationUnit {

	private final String screenName;

	private final String menuItemName;

	DefaultScreenContextConfigurationUnit( Class<?> domainType, final String screenName, final String menuItemName ) {
		super( domainType );

		this.screenName = screenName;
		this.menuItemName = menuItemName;
	}

	@Override
	public String getScreenName() {
		return screenName;
	}

	@Override
	public String getMenuItemName() {
		return menuItemName;
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.SCREEN_CONTEXT;
	}
}