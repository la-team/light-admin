package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.field.DateField;
import org.lightadmin.field.MultiSelect;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.fail;

public abstract class ModificationPage<P extends SecuredPage<P>> extends SecuredPage<P> {

	@FindBy(id = "editForm")
	private WebElement editForm;

	@FindBy(id = "save-changes")
	private WebElement saveButton;

	@FindBy(id = "cancel-changes")
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
		webDriver().clearAndType( getFieldByName( fieldName ), fielValue );
	}

	public ShowViewPage submit() {
		saveButton.click();

		return new ShowViewPage( seleniumContext, domainName, getItemId() ).get();
	}

	protected abstract int getItemId();

	public void clear( String fieldName ) {
		webDriver().clear( getFieldByName( fieldName ) );
	}

	public void check( String booleanField ) {
		getFieldByName( booleanField ).click();
	}

	private WebElement getFieldByName( String booleanField ) {
		return editForm.findElement( By.name( booleanField ) );
	}

	public void select( String fieldName, String value ) {
		new Select( getFieldByName( fieldName ) ).selectByVisibleText( value );
	}

	public void multiSelect( String fieldName, String[] values ) {
		new MultiSelect(
				getMultiSelectElement( fieldName ), webDriver() )
				.multiSelect( values );
	}

	public void deselect( String fieldName ) {
		new Select( getFieldByName( fieldName ) ).selectByIndex( 0 );
	}

	public void clearAllSelections( String fieldName ) {
		new MultiSelect( getMultiSelectElement( fieldName ), webDriver() ).clear();
	}

	public void replaceFieldSelections( String fieldName, String[] optionsToRemove, String[] optionsToAdd ) {
		new MultiSelect( getMultiSelectElement( fieldName ), webDriver() ).replaceSelections( optionsToRemove, optionsToAdd );
	}

	public String selectDateOfCurrentMonth( String fieldName, String date ) {
		return new DateField( editForm.findElement( By.name( fieldName ) ), webDriver() ).selectDateOfCurrentMonth( date );
	}

	private WebElement getMultiSelectElement( String fieldName ) {
		return editForm.findElement(
				By.xpath( "//div[@id='" + fieldName + "-control-group']//div[@class='chzn-container chzn-container-multi']" ) );
	}
}
