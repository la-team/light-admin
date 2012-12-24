package org.lightadmin.page;

import org.lightadmin.SeleniumContext;
import org.lightadmin.component.DashboardStatisticsComponent;
import org.lightadmin.data.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertTrue;

@Component
public class DashboardPage extends SecuredPage<DashboardPage> {

	private final DashboardStatisticsComponent dashboardStatisticsComponent;

	@Autowired
	public DashboardPage( SeleniumContext seleniumContext ) {
		super( seleniumContext );

		dashboardStatisticsComponent = new DashboardStatisticsComponent( seleniumContext );
	}

	public boolean domainLinkDisplayed( Domain domain ) {
		return dashboardStatisticsComponent.domainLinkDisplayed( domain );
	}

	public boolean isDashboardPageLoaded() {
		return webDriver().getCurrentUrl().endsWith( "/dashboard" );
	}

    public int getDomainLinksCount(){
        return dashboardStatisticsComponent.getDomainLinksCount();
    }

	@Override
	protected void load() {
		webDriver().get( baseUrl().toString() + "/dashboard" );
	}

	@Override
	protected void isLoaded() throws Error {
		final String url = webDriver().getCurrentUrl();

		assertTrue( "Not on the Dashboard page: " + url, isDashboardPageLoaded() );
	}

    public int getDomainRecordsCount( Domain domain ) {
        return dashboardStatisticsComponent.getDomainRecordsCount( domain );
    }

    public int getDomainRecordsChange( Domain domain ) {
        return dashboardStatisticsComponent.getDomainRecordsChange( domain );
    }
}