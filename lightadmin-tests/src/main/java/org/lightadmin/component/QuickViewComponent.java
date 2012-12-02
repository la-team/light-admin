package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
		return WebElementTransformer.transformToArray( getQuickViewContainter().findElements( By.tagName( "dt" ) ) );
	}

	public QuickViewComponent hide() {
		getHideButton().click();

		return this;
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			webDriver().waitForElementVisible( getQuickViewContainter() );
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
		return dataTableElement.findElement( By.xpath( "tbody/tr[td[text()=" + itemId + "]]//img[@title='Click to show Info']" ) );
	}

	private WebElement getHideButton() {
		return dataTableElement.findElement( By.xpath( "tbody/tr[td[text()=" + itemId + "]]//img[@title='Click to hide Info']" ) );
	}

	private WebElement getQuickViewContainter() {
		return dataTableElement.findElement( By.id( "quickView-" + itemId ) );
	}
}