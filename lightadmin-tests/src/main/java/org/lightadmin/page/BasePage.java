package org.lightadmin.page;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.net.URL;

public abstract class BasePage<P extends LoadableComponent<P>> extends LoadableComponent<P> {

	protected final WebDriver webDriver;

	protected final URL baseUrl;

	protected BasePage( final WebDriver webDriver, final URL baseUrl ) {
		this.webDriver = webDriver;
		this.baseUrl = baseUrl;

		PageFactory.initElements( this.webDriver, this );
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