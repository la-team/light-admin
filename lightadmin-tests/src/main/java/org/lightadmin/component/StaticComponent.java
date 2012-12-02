package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class StaticComponent implements Component {

	protected SeleniumContext seleniumContext;

	protected StaticComponent( SeleniumContext seleniumContext ) {
		this.seleniumContext = seleniumContext;

		PageFactory.initElements( webDriver(), this );
	}

	protected WebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}
}