package org.lightadmin.field;

import org.lightadmin.util.ExtendedWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MultiSelect {

	private final WebElement webElement;
	private final ExtendedWebDriver webDriver;

	public MultiSelect( WebElement webElement, ExtendedWebDriver webDriver ) {
		this.webElement = webElement;
		this.webDriver = webDriver;
	}

	public void multiSelect( String[] textLabels ) {
		for ( String textLabel : textLabels ) {
			webElement.click();

			WebElement valueList = webElement.findElement( By.className( "chzn-drop" ) );

			valueList.findElement( By.xpath( "//li[contains(text(), '" + textLabel + "')]" ) ).click();

			webDriver.waitForElementVisible( getSelectedOption( textLabel ) );
			webElement.click();
		}
	}

	private WebElement getSelectedOption( String value ) {
		return webElement.findElement( By.xpath( "//span[contains(text(),'" + value + "')]" ) );
	}
}
