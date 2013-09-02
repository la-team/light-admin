package org.lightadmin.page;

import org.apache.commons.lang3.ArrayUtils;
import org.lightadmin.SeleniumContext;
import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.junit.Assert.fail;

public class ShowViewPage extends SecuredPage<ShowViewPage> {

	private String domainName;
	private int itemId;

	@FindBy( id = "data-section" )
	WebElement dataContainer;

	@FindBy( xpath = "//a[@title='Edit']" )
	WebElement editButton;

	protected ShowViewPage( SeleniumContext seleniumContext, String domainName, int itemId ) {
		super( seleniumContext );

		this.domainName = domainName;
		this.itemId = itemId;
	}

	@Override
	protected void load() {
		webDriver().get( baseUrl().toString() + "/domain/" + domainName + "/" + itemId );
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			webDriver().waitForElementVisible( dataContainer );
		} catch ( TimeoutException e ) {
			fail( "Fields are not displayed on ShowViewPage" );
		}

		try {
			webDriver().waitForElementVisible( editButton );
		} catch ( TimeoutException e ) {
			fail( "Edit button is not displayed on ShowViewPage" );
		}
	}

	public String[] getFieldValuesExcludingId() {
		String[] allFieldValues = WebElementTransformer.transformToArray(
				dataContainer.findElements( By.xpath( "//td[contains(@name, 'field-')]" ) ) );

		return ArrayUtils.remove( allFieldValues, 0 );
	}

	public WebElement getField( String fieldName ) {
		return dataContainer.findElement( By.name( "field-" + fieldName ) );
	}
}
