package org.lightadmin.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface ExtendedWebDriver extends WebDriver {

	public void waitForElementVisible( WebElement element );

    public void waitForElementVisible( WebElement element, long timeout );

	public void waitForElementInvisible( WebElement element );

	public boolean isElementPresent( WebElement element );

	public boolean isElementPresent( By by );

	boolean isElementValuePresent( String elementName, String expectedValue );

	public void clearAndType( WebElement element, String text );

	public void clear( WebElement element );

	public void forceFocusOnCurrentWindow();

	public void scrollTo(int pixels);
}
