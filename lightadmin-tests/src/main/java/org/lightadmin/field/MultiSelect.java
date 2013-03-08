package org.lightadmin.field;

import org.lightadmin.util.ExtendedWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MultiSelect {

	private final WebElement webElement;
	private final ExtendedWebDriver webDriver;

	public MultiSelect( WebElement webElement, ExtendedWebDriver webDriver ) {
		this.webElement = webElement;
		this.webDriver = webDriver;
	}

	public void multiSelect( String[] optionNames ) {
		for ( String optionName : optionNames ) {
			addSelection( optionName );
			webElement.click();
		}
	}

	public void clear() {
		List<WebElement> closeButtons = webElement.findElements( By.className( "search-choice-close" ) );

		for ( WebElement closeButton : closeButtons ) {
			closeButton.click();
		}
	}

	public void replaceSelections( String[] optionsToRemove, String[] optionsToAdd ) {
		for ( String optionToRemove : optionsToRemove ) {
			final WebElement selectedOption = getSelectedOption( optionToRemove );

			selectedOption.findElement( By.className( "search-choice-close" ) ).click();
		}
		webElement.click();
		multiSelect( optionsToAdd );
	}

	private void addSelection( String option ) {
		webElement.click();

		WebElement valueList = webElement.findElement( By.className( "chzn-drop" ) );

		valueList.findElement( By.xpath( "//li[contains(text(), '" + option + "')]" ) ).click();

		webDriver.waitForElementVisible( getSelectedOption( option ) );
	}

	private WebElement getSelectedOption( String value ) {
		return webElement.findElement( By.xpath( "//li[span[contains(text(),'" + value + "')]]" ) );
	}
}
