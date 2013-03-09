package org.lightadmin.field;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DateField extends BaseField {

	private final WebElement webElement;

	@FindBy( className = "ui-datepicker-calendar" )
	private WebElement datePicker;

	public DateField( WebElement webElement, SeleniumContext seleniumContext ) {
		super( seleniumContext );

		this.webElement = webElement;
	}

	public String selectDateOfCurrentMonth( String date ) {
		webElement.click();

		try {
			webDriver().waitForElementVisible( datePicker );
		} catch ( TimeoutException e ) {
			webElement.click();
			webDriver().waitForElementVisible( datePicker );
		}

		datePicker.findElement( By.linkText( date ) ).click();
		webDriver().waitForElementInvisible( datePicker );

		return webElement.getAttribute( "value" );
	}
}
