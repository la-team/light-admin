package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FilterFormComponent extends BaseComponent {

	@FindBy( name = "filter-form" )
	private WebElement filterForm;

	@FindBy( id = "apply-filter" )
	private WebElement searchButton;

	@FindBy( id = "reset-filter" )
	private WebElement resetButton;

	public FilterFormComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public void resetFilter() {
		resetButton.click();
	}

	public void filter( String filterField, String filterValue ) {
		WebElement field = filterForm.findElement( By.name( filterField ) );

		clearAndType( field, filterValue );

		searchButton.click();
	}
}