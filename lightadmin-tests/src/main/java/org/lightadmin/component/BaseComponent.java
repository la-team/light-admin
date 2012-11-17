package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.net.URL;

public abstract class BaseComponent implements Component {

	protected SeleniumContext seleniumContext;

	protected BaseComponent( SeleniumContext seleniumContext ) {
		this.seleniumContext = seleniumContext;

		PageFactory.initElements( webDriver(), this );
	}

	protected WebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}

	protected URL baseUrl() {
		return seleniumContext.getBaseUrl();
	}

	protected boolean isElementPresent( WebElement webElement ) {
		try {
			return webElement.isDisplayed();
		} catch ( NoSuchElementException ex ) {
			return false;
		}
	}

	protected void clearAndType( WebElement field, String text ) {
		field.clear();
		field.sendKeys( text );
	}
}