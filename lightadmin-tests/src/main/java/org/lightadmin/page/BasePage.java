package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.util.ExtendedWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.net.URL;

public abstract class BasePage<P extends LoadableComponent<P>> extends LoadableComponent<P> {

	protected final SeleniumContext seleniumContext;

	protected BasePage( SeleniumContext seleniumContext ) {
		this.seleniumContext = seleniumContext;

		PageFactory.initElements( webDriver(), this );
	}

	protected ExtendedWebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}

	protected URL baseUrl() {
		return seleniumContext.getBaseUrl();
	}
}