package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.component.WarningDialog;
import org.lightadmin.field.DateField;
import org.lightadmin.field.MultiSelect;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.springframework.core.io.DefaultResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

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
				getMultiSelectElement( fieldName ), seleniumContext )
				.multiSelect( values );
	}

	public void deselect( String fieldName ) {
		new Select( getFieldByName( fieldName ) ).selectByIndex( 0 );
	}

	public void clearAllSelections( String fieldName ) {
		new MultiSelect( getMultiSelectElement( fieldName ), seleniumContext ).clear();
	}

	public void replaceFieldSelections( String fieldName, String[] optionsToRemove, String[] optionsToAdd ) {
		new MultiSelect( getMultiSelectElement( fieldName ), seleniumContext ).replaceSelections( optionsToRemove, optionsToAdd );
	}

	public String selectDateOfCurrentMonth( String fieldName, String date ) {
		return new DateField( editForm.findElement( By.name( fieldName ) ), seleniumContext ).selectDateOfCurrentMonth( date );
	}

	private WebElement getMultiSelectElement( String fieldName ) {
		return editForm.findElement(
				By.xpath( "//div[@id='" + fieldName + "-control-group']//div[@class='chzn-container chzn-container-multi']" ) );
	}

	public boolean isFieldEditable( String fieldName ) {
		return editForm.findElement( By.id( fieldName ) ).isEnabled();
	}

	public void addValidFile( String fileName ) throws IOException, AWTException {
		addFile( fileName );

		webDriver().waitForElementVisible( editForm.findElement( By.xpath( "//div[contains(text(),'" + fileName + "') and b[contains(text(),'100%')]]" ) ) );
	}

	public WarningDialog addInvalidFile( String fileName ) throws IOException, AWTException {
		addFile( fileName );

		return new WarningDialog( seleniumContext );
	}

	private void addFile( String fileName ) throws AWTException, IOException {
		editForm.findElement( By.id( "picture-pickfiles" ) ).click();

		new FileUploader( fileName ).inputFilePathAndSubmit();
	}

	private class FileUploader {

		private final Robot robot;
		private final String filePath;

		public FileUploader( String fileName ) throws AWTException, IOException {
			this.robot = new Robot();
			this.filePath = new DefaultResourceLoader().getResource( "classpath:testFiles/" + fileName ).getFile().getAbsolutePath();
		}

		public void inputFilePathAndSubmit() {
			clearInputField();

			for ( char ch : filePath.toCharArray() ) {
				if ( Character.isUpperCase( ch ) ) {
					robot.keyPress( KeyEvent.VK_SHIFT );
					robot.keyPress( ( int ) Character.toUpperCase( ch ) );
					robot.keyRelease( ( int ) Character.toUpperCase( ch ) );
					robot.keyRelease( KeyEvent.VK_SHIFT );
				} else {
					robot.keyPress( ( int ) Character.toUpperCase( ch ) );
					robot.keyRelease( ( int ) Character.toUpperCase( ch ) );
				}
			}
			robot.keyPress( KeyEvent.VK_ENTER );
			robot.keyRelease( KeyEvent.VK_ENTER );
		}

		private void clearInputField() {
			robot.keyPress( KeyEvent.VK_CONTROL );
			robot.keyPress( KeyEvent.VK_A );
			robot.keyRelease( KeyEvent.VK_A );
			robot.keyRelease( KeyEvent.VK_CONTROL );
			robot.keyPress( KeyEvent.VK_DELETE );
			robot.keyRelease( KeyEvent.VK_DELETE );
		}
	}
}

