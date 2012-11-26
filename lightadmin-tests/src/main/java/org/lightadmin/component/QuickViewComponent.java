package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class QuickViewComponent extends BaseComponent {

	private final WebElement quickViewContainerElement;

	public QuickViewComponent( final WebElement quickViewContainerElement, SeleniumContext seleniumContext ) {
		super( seleniumContext );
		this.quickViewContainerElement = quickViewContainerElement;

		waitForElementVisible( this.quickViewContainerElement );
	}

	public String[] getQuickViewFieldNames() {
		return WebElementTransformer.transformToArray( quickViewContainerElement.findElements( By.tagName( "dt" ) ) );
	}
}