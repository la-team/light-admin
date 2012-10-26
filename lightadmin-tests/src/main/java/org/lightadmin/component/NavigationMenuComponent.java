package org.lightadmin.component;

import org.lightadmin.data.Domain;
import org.lightadmin.page.ListViewPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;

public class NavigationMenuComponent extends BaseComponent {

	@FindBy( className = "menu-sidenav" )
	private WebElement navigationMenuContainer;

	public NavigationMenuComponent( final WebDriver webDriver, final URL baseUrl ) {
		super( webDriver, baseUrl );
	}

	public ListViewPage navigateToDomain( Domain domain ) {
		navigationMenuContainer.findElement( By.linkText( domain.getLinkText() ) ).click();

		return new ListViewPage( this.webDriver, this.baseUrl, domain );
	}
}