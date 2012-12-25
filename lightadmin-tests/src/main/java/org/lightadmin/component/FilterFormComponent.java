package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FilterFormComponent extends StaticComponent {

	public FilterFormComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public void resetFilter() {
		resetButton().click();
	}

	public void openAdvancedSearch() {
		webDriver().findElement( By.id( "filter-header" ) ).click();
	}

	public void filter( String filterField, String filterValue ) {
		WebElement field = filterForm().findElement( By.name( filterField ) );

		webDriver().clearAndType( field, filterValue );

		searchButton().click();
	}

	private WebElement filterForm() {
		return webDriver().findElement( By.name( "filter-form" ) );
	}

	private WebElement searchButton() {
		return webDriver().findElement( By.id( "apply-filter" ) );
	}

	private WebElement resetButton() {
		return webDriver().findElement( By.id( "reset-filter" ) );
	}
}