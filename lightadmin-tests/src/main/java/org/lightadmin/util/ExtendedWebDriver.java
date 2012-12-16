package org.lightadmin.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface ExtendedWebDriver extends WebDriver {

	public void waitForElementVisible( WebElement element );

	public void waitForElementInvisible( WebElement element );

	public boolean isElementPresent( WebElement element );

	public void clearAndType( WebElement element, String text );
}
