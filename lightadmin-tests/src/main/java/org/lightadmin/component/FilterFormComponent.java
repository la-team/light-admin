package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FilterFormComponent extends StaticComponent {

	@FindBy( id = "filter-header")
	WebElement filerHeader;

	@FindBy( name = "filter-form")
	WebElement filterForm;

	@FindBy( id = "apply-filter")
	WebElement searchButton;

	@FindBy( id = "reset-filter")
	WebElement resetButton;

	public FilterFormComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public void resetFilter() {
		resetButton.click();
	}

	public void openAdvancedSearch() {
		if ( !filerHeader.getAttribute( "class" ).equals( "head closed inactive" )) {
			filerHeader.click();
			webDriver().waitForElementVisible( filterForm );
		}
	}

	public void filter( String filterField, String filterValue ) {
		WebElement field = filterForm.findElement( By.name( filterField ) );

		webDriver().clearAndType( field, filterValue );

		searchButton.click();
	}

	public String[] getCaptions() {
		return WebElementTransformer.transformToArray( filterForm.findElements( By.id( "filter-field-name" ) ) );
	}
}