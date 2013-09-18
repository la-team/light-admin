package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.data.Domain;
import org.lightadmin.page.DashboardPage;
import org.lightadmin.page.ListViewPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NavigationMenuComponent extends StaticComponent {

	@FindBy( id = "menu" )
	private WebElement navigationMenuContainer;

	public NavigationMenuComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public ListViewPage navigateToDomain( Domain domain ) {
		getNavigationLink( domain.getLinkText() ).click();

		return new ListViewPage( seleniumContext, domain ).get();
	}

	public DashboardPage navigateToDashboard() {
		getNavigationLink( "Dashboard" ).click();

		return new DashboardPage( seleniumContext );
	}

	private WebElement getNavigationLink( String linkText ) {
		return navigationMenuContainer.findElement( By.linkText( linkText ) );
	}
}