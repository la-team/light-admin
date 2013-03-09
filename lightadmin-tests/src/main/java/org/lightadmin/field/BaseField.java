package org.lightadmin.field;

import org.lightadmin.SeleniumContext;
import org.lightadmin.util.ExtendedWebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BaseField {

	private final SeleniumContext seleniumContext;

	public BaseField(SeleniumContext seleniumContext ) {
		this.seleniumContext = seleniumContext;

		PageFactory.initElements( webDriver(), this );
	}

	public ExtendedWebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}
}
