package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.component.WarningDialog;
import org.lightadmin.field.BaseSelect;
import org.lightadmin.field.DateField;
import org.lightadmin.field.MultiSelect;
import org.lightadmin.field.SmartSelect;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;
import org.springframework.core.io.DefaultResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static org.junit.Assert.fail;

public abstract class ModificationPage<P extends SecuredPage<P>> extends SecuredPage<P> {

	@FindBy( className = "mainForm" )
	private WebElement editForm;

	@FindBy( name = "save-changes" )
	private WebElement saveButton;

	@FindBy( name = "cancel-changes" )
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

		webDriver().forceFocusOnCurrentWindow();
	}

	public String getDomainName() {
		return domainName;
	}

	public void type( String fieldName, String fielValue ) {
		webDriver().clearAndType( getFieldByName( fieldName ), fielValue );
	}

	public ShowViewPage submit() {
		webDriver().scrollTo(500);
		((Locatable) saveButton).getCoordinates().inViewPort();
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

	public void select( String fieldName, String... values ) {
		getSelectField( fieldName ).select( values );
	}

	public void clearSelection( String fieldName ) {
		getSelectField( fieldName ).clear();
	}

	public void replaceFieldSelections( String fieldName, String[] optionsToRemove, String[] optionsToAdd ) {
		new MultiSelect( getMultiSelectElement( fieldName ), seleniumContext ).replaceSelections( optionsToRemove, optionsToAdd );
	}

	public String selectDateOfCurrentMonth( String fieldName, String date ) {
		return new DateField( editForm.findElement( By.name( fieldName ) ), seleniumContext ).selectDateOfCurrentMonth( date );
	}

	public boolean isFieldEditable( String fieldName ) {
		return editForm.findElement( By.id( fieldName ) ).isEnabled();
	}

    public boolean isFieldDisplayed( String fieldName ) {
        return editForm.findElement( By.id( fieldName ) ).isDisplayed();
    }

    public boolean isFieldReanOnly(String fieldName) {
        return "true".equals(editForm.findElement( By.id( fieldName ) ).getAttribute("readonly"));
    }

	public void addValidFile( String fileName ) throws IOException, AWTException {
		addFile( fileName );

		webDriver().waitForElementVisible( editForm.findElement( By.xpath( "//div[contains(text(),'" + fileName + "') and b[contains(text(),'100%')]]" ) ) );
	}

	public WarningDialog addInvalidFile( String fileName ) throws IOException, AWTException {
		addFile( fileName );

		return new WarningDialog( seleniumContext );
	}

	private BaseSelect getSelectField( String fieldName ) {
		final WebElement theSelect = editForm.findElement( By.xpath( "//div[@id='" + fieldName + "-control-group']//div[contains(@class,'chzn-container')]" ) );
		if ( theSelect.getAttribute( "class" ).contains( "multi" ) ) {
			return new MultiSelect( theSelect, seleniumContext );
		} else
			return new SmartSelect( theSelect, seleniumContext );
	}

	private WebElement getMultiSelectElement( String fieldName ) {
		return editForm.findElement(
				By.xpath( "//div[@id='" + fieldName + "-control-group']//div[@class='chzn-container chzn-container-multi']" ) );
	}

	private WebElement getFieldByName( String fieldName ) {
		return editForm.findElement( By.name( fieldName ) );
	}

	private void addFile( String fileName ) throws AWTException, IOException {
		editForm.findElement( By.id( "picture-pickfiles" ) ).click();

		new FileUploader( fileName ).inputFilePathAndSubmit();
	}

	public void searchAndSelect( String fieldName, String searchString, String labelToSelect ) {
		getSelectField( fieldName ).searchAndSelect( searchString, labelToSelect );
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

