package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.data.Domain;
import org.lightadmin.page.ListViewPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NavigationMenuComponent extends BaseComponent {

	@FindBy( className = "menu-sidenav" )
	private WebElement navigationMenuContainer;

	public NavigationMenuComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public ListViewPage navigateToDomain( Domain domain ) {
		navigationMenuContainer.findElement( By.linkText( domain.getLinkText() ) ).click();

		return new ListViewPage( seleniumContext, domain ).get();
	}
}