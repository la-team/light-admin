package org.lightadmin.util;

import com.google.common.base.Predicate;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ExtendedWebDriverImpl implements ExtendedWebDriver {

	private WebDriver webDriver;
	private long webDriverTimeout;

	public ExtendedWebDriverImpl( WebDriver webDriver, long webDriverTimeout ) {
		this.webDriver = webDriver;
		this.webDriverTimeout = webDriverTimeout;
	}

	@Override
	public boolean isElementPresent( WebElement webElement ) {
		try {
			return webElement.isDisplayed();
		} catch ( NoSuchElementException ex ) {
			return false;
		}
	}

	@Override
	public void clearAndType( WebElement field, String text ) {
		field.clear();
		field.sendKeys( text );
	}

	@Override
	public void waitForElementVisible( final WebElement element ) throws TimeoutException {
		new WebDriverWait( webDriver, webDriverTimeout ).until( new Predicate<WebDriver>() {
			@Override
			public boolean apply( @Nullable WebDriver input ) {
				return element.isDisplayed();
			}
		} );
	}

	@Override
	public void get( String url ) {
		webDriver.get( url );
	}

	@Override
	public String getCurrentUrl() {
		return webDriver.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		return webDriver.getTitle();
	}

	@Override
	public List<WebElement> findElements( By by ) {
		return webDriver.findElements( by );
	}

	@Override
	public WebElement findElement( By by ) {
		return webDriver.findElement( by );
	}

	@Override
	public String getPageSource() {
		return webDriver.getPageSource();
	}

	@Override
	public void close() {
		webDriver.close();
	}

	@Override
	public void quit() {
		webDriver.quit();
	}

	@Override
	public Set<String> getWindowHandles() {
		return webDriver.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return webDriver.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		return webDriver.switchTo();
	}

	@Override
	public Navigation navigate() {
		return webDriver.navigate();
	}

	@Override
	public Options manage() {
		return webDriver.manage();
	}
}
