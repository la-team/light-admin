package org.lightadmin.page;

import org.lightadmin.component.DashboardStatisticsComponent;
import org.lightadmin.data.Domain;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

import static org.junit.Assert.assertTrue;

@Component
public class DashboardPage extends SecuredPage<DashboardPage> {

	private final DashboardStatisticsComponent dashboardStatisticsComponent;

	@Autowired
	public DashboardPage( WebDriver driver, URL baseUrl ) {
		super( driver, baseUrl );

		dashboardStatisticsComponent = new DashboardStatisticsComponent( driver, baseUrl );
	}

	public boolean domainLinkDisplayed( Domain domain ) {
		return dashboardStatisticsComponent.domainLinkDisplayed( domain );
	}

	public boolean isDashboardPageLoaded() {
		return webDriver.getCurrentUrl().endsWith( "/dashboard" );
	}

    public int getDomainLinksCount(){
        return dashboardStatisticsComponent.getDomainLinksCount();
    }

	@Override
	protected void load() {
		webDriver.get( baseUrl.toString() + "/dashboard" );
	}

	@Override
	protected void isLoaded() throws Error {
		final String url = webDriver.getCurrentUrl();

		assertTrue( "Not on the Dashboard page: " + url, isDashboardPageLoaded() );
	}

    public boolean isProgressBarDisplayed( Domain domain ) {
        return dashboardStatisticsComponent.isProgressBarDisplayed( domain );
    }

    public int getDomainRecordsCount( Domain domain ) {
        return dashboardStatisticsComponent.getDomainRecordsCount( domain );
    }

    public int getDomainRecordsPercentage(Domain domain) {
        return dashboardStatisticsComponent.getDomainRecordsPercentage( domain );
    }
}