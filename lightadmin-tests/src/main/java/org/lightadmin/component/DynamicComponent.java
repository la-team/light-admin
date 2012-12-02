package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.util.ExtendedWebDriver;
import org.openqa.selenium.support.ui.LoadableComponent;

public abstract class DynamicComponent<C extends LoadableComponent<C>> extends LoadableComponent<C> implements Component {

	private SeleniumContext seleniumContext;

	protected DynamicComponent( SeleniumContext seleniumContext ) {
		this.seleniumContext = seleniumContext;
	}

	protected ExtendedWebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}
}
