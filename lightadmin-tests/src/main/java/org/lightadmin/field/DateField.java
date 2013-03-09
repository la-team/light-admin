package org.lightadmin.field;

import org.lightadmin.util.ExtendedWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DateField extends BaseField {

	public DateField( WebElement webElement, ExtendedWebDriver webDriver ) {
		super( webElement, webDriver );
	}

	public String selectDateOfCurrentMonth( String date ) {
		webElement().click();
		WebElement datePicker = webDriver().findElement( By.className( "ui-datepicker-calendar" ) );
		datePicker.findElement( By.linkText( date ) ).click();
		webDriver().waitForElementInvisible( datePicker );

		return webElement().getAttribute( "value" );
	}
}
