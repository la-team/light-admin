package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.fail;

public class QuickViewComponent extends DynamicComponent<QuickViewComponent> {

	private final WebElement dataTableElement;
	private final int itemId;

	public QuickViewComponent( int itemId, WebElement dataTableElement, SeleniumContext seleniumContext ) {
		super( seleniumContext );

		this.itemId = itemId;
		this.dataTableElement = dataTableElement;
	}

	public String[] getQuickViewFieldNames() {
		return WebElementTransformer.transformToArray( getQuickViewContainer().findElements( By.className( "qv-field-name" ) ) );
	}

	public String[] getQuickViewFieldValues() {
		return WebElementTransformer.transformToArray( getQuickViewContainer().findElements( By.className( "qv-field-value" ) ) );
	}

	public QuickViewComponent hide() {
		getHideButton().click();

		return this;
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			webDriver().waitForElementVisible( getQuickViewContainer() );
		} catch ( NoSuchElementException e ) {
			fail( "Quick View Component for is not visible" );
		}
		try {
			webDriver().waitForElementVisible( getHideButton() );
		} catch ( NoSuchElementException e ) {
			fail( "Hide button is not visible" );
		}
	}

	@Override
	protected void load() {
		getShowButton().click();
	}

	private WebElement getShowButton() {
		return dataTableElement.findElement( By.xpath( ".//img[@title='Click for Quick View']" ) );
	}

	private WebElement getHideButton() {
		return dataTableElement.findElement( By.xpath( ".//img[@title='Click to close Quick View']" ) );
	}

	private WebElement getQuickViewContainer() {
		return webDriver().findElement( By.id( "quickView-" + itemId ) );
	}

	public boolean isHidden() {
		try {
			webDriver().waitForElementInvisible( getQuickViewContainer() );
			webDriver().waitForElementVisible( getShowButton() );
		} catch ( TimeoutException e ) {
			return false;
		} catch ( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	public WebElement getField( String fieldName ) {
		return webDriver().findElement(
				By.xpath( "//div[@id='quickView-" + itemId + "']//tr[td[strong[contains(text(),'" + fieldName + "')]]]/td[@class='qv-field-value']" ) );
	}
}