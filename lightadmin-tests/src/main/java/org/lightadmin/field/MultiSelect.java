package org.lightadmin.field;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class MultiSelect extends BaseField implements BaseSelect {

	private final WebElement theField;

	@FindBy(className = "chzn-drop")
	private WebElement valueList;

	public MultiSelect( WebElement webElement, SeleniumContext seleniumContext ) {
		super( seleniumContext );
		this.theField = webElement;
	}

	@Override
	public void select( String... optionNames ) {
		for ( String optionName : optionNames ) {
			addSelection( optionName );
			theField.click();
		}
	}

	@Override
	public void clear() {
		List<WebElement> closeButtons = theField.findElements( By.className( "search-choice-close" ) );

		for ( WebElement closeButton : closeButtons ) {
			closeButton.click();
		}
	}

	@Override
	public void searchAndSelect( String searchString, String labelToSelect ) {
		theField.sendKeys( searchString );
		select( labelToSelect );
	}

	public void replaceSelections( String[] optionsToRemove, String[] optionsToAdd ) {
		for ( String optionToRemove : optionsToRemove ) {
			final WebElement selectedOption = getSelectedOption( optionToRemove );

			selectedOption.findElement( By.className( "search-choice-close" ) ).click();
		}
		theField.click();
		select( optionsToAdd );
	}

	private void addSelection( String option ) {
		theField.click();

		webDriver().waitForElementVisible( valueList );

		final WebElement optionToSelect = valueList.findElement( By.xpath( "//li[contains(text(), '" + option + "')]" ) );

		( ( Locatable ) optionToSelect ).getCoordinates().inViewPort();
		optionToSelect.click();

		webDriver().waitForElementVisible( getSelectedOption( option ) );
	}

	private WebElement getSelectedOption( String value ) {
		return theField.findElement( By.xpath( "//li[span[contains(text(),'" + value + "')]]" ) );
	}
}
