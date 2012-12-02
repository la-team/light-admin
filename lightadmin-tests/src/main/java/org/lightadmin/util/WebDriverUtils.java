package org.lightadmin.util;

import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;

public class WebDriverUtils {

	public static boolean isElementPresent( WebElement webElement ) {
		try {
			return webElement.isDisplayed();
		} catch ( NoSuchElementException ex ) {
			return false;
		}
	}

	public static void waitForElementVisible( final WebElement element, final WebDriver webDriver, final long timeout ) throws TimeoutException {
		new WebDriverWait( webDriver, timeout ).until( new Predicate<WebDriver>() {
			@Override
			public boolean apply( @Nullable WebDriver input ) {
				return element.isDisplayed();
			}
		} );
	}

	public static void clearAndType( WebElement field, String text ) {
		field.clear();
		field.sendKeys( text );
	}
}
