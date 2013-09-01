package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.component.BreadcrumbsComponent;
import org.lightadmin.component.NavigationMenuComponent;
import org.lightadmin.component.TopNavigationComponent;
import org.lightadmin.data.Domain;

public abstract class SecuredPage<P extends BasePage<P>> extends BasePage<P> {

	private final NavigationMenuComponent navigationMenuComponent;

	private final BreadcrumbsComponent breadcrumbsComponent;

	private final TopNavigationComponent topNavigationComponent;

	protected SecuredPage( SeleniumContext seleniumContext ) {
		super( seleniumContext );

		navigationMenuComponent = new NavigationMenuComponent( seleniumContext );
		breadcrumbsComponent = new BreadcrumbsComponent( seleniumContext );
		topNavigationComponent = new TopNavigationComponent( seleniumContext );
	}

	public boolean dashboardBreadcrumbItemLinkPresent() {
		return breadcrumbsComponent.dashboardBreadcrumbLinkPresent();
	}

	public ListViewPage navigateToDomain( Domain domain ) {
		return navigationMenuComponent.navigateToDomain( domain );
	}

	public DashboardPage navigateToDashboard() {
		return navigationMenuComponent.navigateToDashboard();
	}

	public boolean isLoggedIn() {
		return topNavigationComponent.isLoggedIn();
	}

	public LoginPage logout() {
		return topNavigationComponent.logout();
	}
}