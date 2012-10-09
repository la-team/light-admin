package org.lightadmin.core.config.domain.context;

public class ScreenContext {

	private final String screenName;

	private final String menuItemName;

	ScreenContext( final String screenName, final String menuItemName ) {
		this.screenName = screenName;
		this.menuItemName = menuItemName;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

}