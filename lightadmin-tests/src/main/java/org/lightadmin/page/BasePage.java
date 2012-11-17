package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.net.URL;

public abstract class BasePage<P extends LoadableComponent<P>> extends LoadableComponent<P> {

	protected final SeleniumContext seleniumContext;

	protected BasePage( SeleniumContext seleniumContext ) {
		this.seleniumContext = seleniumContext;

		PageFactory.initElements( webDriver(), this );
	}

	protected WebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}

	protected URL baseUrl() {
		return seleniumContext.getBaseUrl();
	}

	protected long webDriverWaitTimeout() {
		return seleniumContext.getWebDriverWaitTimeout();
	}

	protected void clearAndType( WebElement field, String text ) {
		field.clear();
		field.sendKeys( text );
	}

	protected boolean isElementPresent( WebElement webElement ) {
		try {
			return webElement.isDisplayed();
		} catch ( NoSuchElementException ex ) {
			return false;
		}
	}
}