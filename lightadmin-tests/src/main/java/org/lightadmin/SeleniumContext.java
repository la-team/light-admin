package org.lightadmin;

import org.openqa.selenium.WebDriver;

import java.net.URL;

public class SeleniumContext {

	private final WebDriver webDriver;
	private final URL baseUrl;
	private final long webDriverWaitTimeout;

	public SeleniumContext( final WebDriver webDriver, final URL baseUrl, final long webDriverWaitTimeout ) {
		this.webDriver = webDriver;
		this.baseUrl = baseUrl;
		this.webDriverWaitTimeout = webDriverWaitTimeout;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public URL getBaseUrl() {
		return baseUrl;
	}

	public long getWebDriverWaitTimeout() {
		return webDriverWaitTimeout;
	}

	public void destroy() {
		webDriver.quit();
	}
}