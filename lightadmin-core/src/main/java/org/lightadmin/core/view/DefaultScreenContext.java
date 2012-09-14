package org.lightadmin.core.view;

import java.util.concurrent.Callable;

public class DefaultScreenContext implements ScreenContext {

	private String screenName;
	private String menuName;
	private boolean displayInMenu;

	@Override
	public ScreenContext screenName( final String screenName ) {
		this.screenName = screenName;
		return this;
	}

	@Override
	public ScreenContext menuName( final String menuName ) {
		this.menuName = menuName;
		return this;
	}

	@Override
	public ScreenContext displayInMenu( final Callable<Boolean> callable ) {
		try {
			this.displayInMenu = callable.call();
		} catch ( Exception e ) {
			// nop
		}
		return this;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getMenuName() {
		return menuName;
	}

	public boolean isDisplayInMenu() {
		return displayInMenu;
	}
}