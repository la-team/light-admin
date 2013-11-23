package org.lightadmin.field;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DateField extends BaseField {

	private final WebElement dateField;

	@FindBy( className = "ui-datepicker-calendar" )
	private WebElement datePicker;

	public DateField( WebElement theDateField, SeleniumContext seleniumContext ) {
		super( seleniumContext );

		this.dateField = theDateField;
	}

	public String selectDateOfCurrentMonth( String date ) {
		dateField.click();
		webDriver().waitForElementVisible( datePicker );

		datePicker.findElement( By.linkText( date ) ).click();
		webDriver().waitForElementInvisible( datePicker );

		return dateField.getAttribute( "value" );
	}
}
