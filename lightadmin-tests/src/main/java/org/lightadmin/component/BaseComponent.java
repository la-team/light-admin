package org.lightadmin.component;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.net.URL;

public abstract class BaseComponent implements Component {

	protected final WebDriver webDriver;
	protected final URL baseUrl;

	protected BaseComponent( final WebDriver webDriver, final URL baseUrl ) {
		this.webDriver = webDriver;
		this.baseUrl = baseUrl;

		PageFactory.initElements( webDriver, this );
	}

	protected boolean isElementPresent( WebElement webElement ) {
		try {
			return webElement.isDisplayed();
		} catch ( NoSuchElementException ex ) {
			return false;
		}
	}
}