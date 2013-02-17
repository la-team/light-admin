package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.LoadableComponent;

import static org.junit.Assert.fail;

public abstract class ModificationPage<P extends SecuredPage<P>> extends SecuredPage<P> {

	@FindBy( id = "editForm" )
	private WebElement editForm;

	@FindBy( id = "save-changes" )
	private WebElement saveButton;

	@FindBy( id = "cancel-changes" )
	private WebElement cancelButton;

	private String domainName;

	protected ModificationPage( SeleniumContext seleniumContext, String domainName ) {
		super( seleniumContext );

		this.domainName = domainName;
	}

	public void isLoaded() {
		try {
			webDriver().waitForElementVisible( editForm );
		} catch ( TimeoutException e ) {
			fail( "Edit form is not displayed" );
		}

		try {
			webDriver().waitForElementVisible( saveButton );
		} catch ( TimeoutException e ) {
			fail( "Save button is not displayed" );
		}
	}

	public String getDomainName() {
		return domainName;
	}

	public void type( String fieldName, String fielValue ) {
		webDriver().clearAndType( webDriver().findElement( By.name( fieldName ) ), fielValue );
	}

	public ShowViewPage submit() {
		saveButton.click();

		return new ShowViewPage( seleniumContext, domainName, getItemId() ).get();
	}

	protected abstract int getItemId();
}
