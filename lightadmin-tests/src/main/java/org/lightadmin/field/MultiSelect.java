package org.lightadmin.field;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MultiSelect extends BaseField {

	private final WebElement webElement;

	@FindBy(className = "chzn-drop")
	private WebElement valueList;

	public MultiSelect( WebElement webElement, SeleniumContext seleniumContext ) {
		super( seleniumContext );
		this.webElement = webElement;
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

		try {
			webDriver().waitForElementVisible( valueList );
		} catch ( TimeoutException e ) {
			webElement.click();
			webDriver().waitForElementVisible( valueList );
		}

		valueList.findElement( By.xpath( "//li[contains(text(), '" + option + "')]" ) ).click();

		webDriver().waitForElementVisible( getSelectedOption( option ) );
	}

	private WebElement getSelectedOption( String value ) {
		return webElement.findElement( By.xpath( "//li[span[contains(text(),'" + value + "')]]" ) );
	}
}
