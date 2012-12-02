package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.LoadableComponent;

public abstract class DynamicComponent<C extends LoadableComponent<C>> extends LoadableComponent<C> implements Component {

	private SeleniumContext seleniumContext;

	protected DynamicComponent( SeleniumContext seleniumContext ) {
		this.seleniumContext = seleniumContext;
	}

	protected WebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}

	protected long webDriverTimeout() {
		return seleniumContext.getWebDriverWaitTimeout();
	}
}
