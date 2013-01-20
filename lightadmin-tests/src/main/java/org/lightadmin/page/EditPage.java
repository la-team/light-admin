package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.junit.Assert.fail;

public class EditPage extends BasePage<EditPage> {

	private String domainName;
	private int itemId;

	@FindBy( id = "editForm" )
	private WebElement editForm;

	@FindBy( id = "save-changes" )
	private WebElement saveButton;

	@FindBy( id = "cancel-changes" )
	private WebElement cancelButton;

	public EditPage( SeleniumContext seleniumContext, String domainName, int itemId ) {
		super( seleniumContext );

		this.domainName = domainName;
		this.itemId = itemId;
	}

	@Override
	protected void load() {
		webDriver().get( baseUrl() + "/domain/" + domainName + "/" + itemId + "/edit" );
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			webDriver().waitForElementVisible( editForm );
		} catch ( TimeoutException e ) {
			fail( "Edit form id not displayed" );
		}

		try {
			webDriver().waitForElementVisible( saveButton );
		} catch ( TimeoutException e ) {
			fail( "Save button is not displayed" );
		}

		try {
			webDriver().waitForElementVisible( cancelButton );
		} catch ( TimeoutException e ) {
			fail( "Cancel button is not displayed" );
		}
	}

	public boolean isFieldEditable( String fieldName ) {
		return editForm.findElement( By.name( fieldName ) ).isEnabled();
	}
}
