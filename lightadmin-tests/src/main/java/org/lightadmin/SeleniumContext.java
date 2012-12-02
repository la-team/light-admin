package org.lightadmin;

import org.lightadmin.util.ExtendedWebDriver;
import org.lightadmin.util.ExtendedWebDriverImpl;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public class SeleniumContext {

	private final ExtendedWebDriver webDriver;
	private final URL baseUrl;

	public SeleniumContext( final WebDriver webDriver, final URL baseUrl, final long webDriverWaitTimeout ) {
		this.webDriver = new ExtendedWebDriverImpl( webDriver, webDriverWaitTimeout );
		this.baseUrl = baseUrl;
	}

	public ExtendedWebDriver getWebDriver() {
		return webDriver;
	}

	public URL getBaseUrl() {
		return baseUrl;
	}

	public void destroy() {
		webDriver.quit();
	}
}