package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.component.BreadcrumbsComponent;
import org.lightadmin.component.NavigationMenuComponent;
import org.lightadmin.component.ToolbarComponent;
import org.lightadmin.data.Domain;

public abstract class SecuredPage<P extends BasePage<P>> extends BasePage<P> {

	private final NavigationMenuComponent navigationMenuComponent;

	private final BreadcrumbsComponent breadcrumbsComponent;

	private final ToolbarComponent toolbarComponent;

	protected SecuredPage( SeleniumContext seleniumContext ) {
		super( seleniumContext );

		navigationMenuComponent = new NavigationMenuComponent( seleniumContext );
		breadcrumbsComponent = new BreadcrumbsComponent( seleniumContext );
		toolbarComponent = new ToolbarComponent( seleniumContext );
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