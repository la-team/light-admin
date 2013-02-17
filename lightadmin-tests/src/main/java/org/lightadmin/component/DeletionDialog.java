package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeletionDialog extends StaticComponent {

	@FindBy( id = "popup_container" )
	private WebElement container;

	@FindBy( id = "popup_ok" )
	private WebElement confirmButton;

	@FindBy( id = "popup_cancel" )
	private WebElement cancelButton;

	private WebElement rowForItem;

	protected DeletionDialog( WebElement rowForItem, SeleniumContext seleniumContext ) {
		super( seleniumContext );

		this.rowForItem = rowForItem;
	}


	public void confirm() {
		showDialog();

		confirmButton.click();

		waitForDialogToClose();
	}

	public void cancel() {
		showDialog();

		cancelButton.click();

		waitForDialogToClose();
	}

	private void showDialog() {
		rowForItem.findElement( By.xpath( ".//a[@title='Remove']" ) ).click();

		webDriver().waitForElementVisible( container );
	}

	private void waitForDialogToClose() {
		webDriver().waitForElementInvisible( container );
	}
}
