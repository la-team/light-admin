package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WarningDialog extends StaticComponent {

	@FindBy( id = "popup_container" )
	private WebElement container;

	@FindBy( id = "popup_ok" )
	private WebElement confirmButton;

	@FindBy( id = "popup_message" )
	private WebElement message;

	public WarningDialog( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public String getMessage() {
		webDriver().waitForElementVisible( container );

		return message.getText();
	}

	public boolean isPresent() {
		return webDriver().isElementPresent( container );
	}

	public void close() {
		confirmButton.click();
	}
}
