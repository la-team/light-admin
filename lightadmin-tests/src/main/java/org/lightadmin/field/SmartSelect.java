package org.lightadmin.field;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;

public class SmartSelect extends BaseField implements BaseSelect {

	private final WebElement theElement;

	@FindBy( className = "chzn-results" )
	private WebElement valueList;

	@FindBy( className = "search-choice-close" )
	private WebElement clearButton;

	public SmartSelect( WebElement theElement, SeleniumContext seleniumContext ) {
		super( seleniumContext );
		this.theElement = theElement;
	}

	@Override
	public void select( String... optionNames ) {
		theElement.click();
		webDriver().waitForElementVisible( valueList );

		final WebElement optionToSelect = valueList.findElement( By.xpath( String.format( "//li[contains(text(),'%s')]", optionNames[ 0 ] ) ) );

		( ( Locatable ) optionToSelect ).getCoordinates().inViewPort();

		optionToSelect.click();

		webDriver().waitForElementVisible( getSelectedOption( optionNames[ 0 ] ) );
	}

	@Override
	public void clear() {
		clearButton.click();
		webDriver().waitForElementVisible( getSelectedOption( "Select " ) );
	}

	private WebElement getSelectedOption( String textLabel ) {
		return theElement.findElement( By.xpath( String.format( "//a[span[contains(text(),'%s')]]", textLabel ) ) );
	}
}
