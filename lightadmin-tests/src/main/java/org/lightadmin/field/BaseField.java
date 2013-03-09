package org.lightadmin.field;

import org.lightadmin.util.ExtendedWebDriver;
import org.openqa.selenium.WebElement;

public abstract class BaseField {

	private final WebElement webElement;
	private final ExtendedWebDriver webDriver;

	public BaseField( WebElement webElement, ExtendedWebDriver webDriver ) {
		this.webElement = webElement;
		this.webDriver = webDriver;
	}

	public WebElement webElement() {
		return webElement;
	}

	public ExtendedWebDriver webDriver() {
		return webDriver;
	}
}
