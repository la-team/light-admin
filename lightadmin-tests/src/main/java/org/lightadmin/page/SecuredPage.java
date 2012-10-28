package org.lightadmin.page;

import org.lightadmin.component.BreadcrumbsComponent;
import org.lightadmin.component.NavigationMenuComponent;
import org.lightadmin.component.ToolbarComponent;
import org.lightadmin.data.Domain;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public abstract class SecuredPage<P extends BasePage<P>> extends BasePage<P> {

	private final NavigationMenuComponent navigationMenuComponent;

	private final BreadcrumbsComponent breadcrumbsComponent;

	private final ToolbarComponent toolbarComponent;

	protected SecuredPage( final WebDriver driver, final URL baseUrl ) {
		super( driver, baseUrl );

		navigationMenuComponent = new NavigationMenuComponent( driver, baseUrl );
		breadcrumbsComponent = new BreadcrumbsComponent( driver, baseUrl );
		toolbarComponent = new ToolbarComponent( driver, baseUrl );
	}

	public boolean dashboardBreadcrumbItemPresent() {
		return breadcrumbsComponent.dashboardBreadcrumbItemPresent();
	}

	public ListViewPage navigateToDomain( Domain domain ) {
		return navigationMenuComponent.navigateToDomain( domain );
	}

	public boolean isLoggedIn() {
		return toolbarComponent.isLoggedIn();
	}

	public LoginPage logout() {
		return toolbarComponent.logout();
	}
}