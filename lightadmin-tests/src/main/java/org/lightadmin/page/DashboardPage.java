package org.lightadmin.page;

import org.lightadmin.component.DashboardStatisticsComponent;
import org.lightadmin.component.NavigationMenuComponent;
import org.lightadmin.data.Domain;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

import static org.junit.Assert.assertTrue;

@Component
public class DashboardPage extends BasePage<DashboardPage> {

	private final NavigationMenuComponent navigationMenuComponent;

	private DashboardStatisticsComponent dashboardStatisticsComponent;

	@Autowired
	public DashboardPage( WebDriver driver, URL baseUrl ) {
		super( driver, baseUrl );

		navigationMenuComponent = new NavigationMenuComponent( driver, baseUrl );
		dashboardStatisticsComponent = new DashboardStatisticsComponent( driver, baseUrl );
	}

	public boolean domainLinkDisplayed( Domain domain ) {
		return dashboardStatisticsComponent.domainLinkDisplayed( domain );
	}

	public ListViewPage navigateToDomain( Domain domain ) {
		return navigationMenuComponent.navigateToDomain( domain );
	}

	public boolean isDashboardPageLoaded() {
		return webDriver.getCurrentUrl().endsWith( "/dashboard" );
	}

	@Override
	protected void loadPage() {
		webDriver.get( baseUrl.toString() + "/dashboard" );
	}

	@Override
	protected void isLoaded() throws Error {
		final String url = webDriver.getCurrentUrl();

		assertTrue( "Not on the Dashboard page: " + url, isDashboardPageLoaded() );
	}
}