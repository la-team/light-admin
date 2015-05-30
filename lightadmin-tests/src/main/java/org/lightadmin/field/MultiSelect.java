package org.lightadmin.field;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

import java.util.List;

public class MultiSelect extends BaseField implements BaseSelect {

	private final WebElement theField;

	public MultiSelect( WebElement webElement, SeleniumContext seleniumContext ) {
		super( seleniumContext );
		this.theField = webElement;
	}

	@Override
	public void select( String... optionNames ) {
		for (int i = 0, optionNamesLength = optionNames.length; i < optionNamesLength; i++) {
			String optionName = optionNames[i];
			addSelection(optionName);

			if (i < optionNamesLength - 1) {
				// do not re-focus for last option
				theField.click();
			}
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
		theField.click();
		theField.findElement(By.tagName("input")).sendKeys(searchString);
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

	private WebElement findOptionByText(WebElement valueList, String text) {
		for (WebElement listItem : valueList.findElements(By.xpath(".//li"))) {
			if (listItem.getText().contains(text)) {
				// ok, we found it
				return listItem;
			}
		}
		return null;
	}

	private void addSelection( String option ) {
		theField.click();

		WebElement valueList = theField.findElement(By.className("chzn-drop"));

		webDriver().waitForElementVisible( valueList, 10 );

		final WebElement optionToSelect = findOptionByText(valueList, option);
		assert optionToSelect != null;

		( ( Locatable ) optionToSelect ).getCoordinates().inViewPort();
		optionToSelect.click();

		webDriver().waitForElementVisible( getSelectedOption( option ) );
	}

	private WebElement getSelectedOption( String value ) {
		return theField.findElement( By.xpath( "//li[span[contains(text(),'" + value + "')]]" ) );
	}
}
