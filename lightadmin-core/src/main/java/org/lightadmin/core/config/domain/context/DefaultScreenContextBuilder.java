package org.lightadmin.core.config.domain.context;

public class DefaultScreenContextBuilder implements ScreenContextBuilder {

	private String screenName = "Undefined";

	private String menuItemName = "Undefined";

	@Override
	public ScreenContextBuilder screenName( final String screenName ) {
		this.screenName = screenName;
		return this;
	}

	@Override
	public ScreenContextBuilder menuName( final String menuItemName ) {
		this.menuItemName = menuItemName;
		return this;
	}

	@Override
	public ScreenContext build() {
		return new ScreenContext( screenName, menuItemName );
	}
}