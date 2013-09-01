package org.lightadmin.page;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.CustomerTestEntityConfiguration;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.config.OrderTestEntityConfiguration;
import org.lightadmin.data.Domain;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.lightadmin.data.Domain.*;

@RunWithConfiguration( {
						   OrderTestEntityConfiguration.class, CustomerTestEntityConfiguration.class,
						   FilterTestEntityConfiguration.class
					   } )
@LoginOnce( domain = Domain.TEST_CUSTOMERS )
public class DashboardPageTest extends SeleniumIntegrationTest {

	private final List<Domain> expectedDomains = newArrayList( TEST_ORDERS.setExpectedRecordCount( 8 ), TEST_CUSTOMERS.setExpectedRecordCount( 29 ), FILTER_TEST_DOMAIN.setExpectedRecordCount( 11 ) );

	private DashboardPage dashboardPage;

	@Before
	public void setup() throws Exception {
		//todo: ikostenko: test should navigate to dashboard only once, shouldn't navigate to domain
		dashboardPage = getStartPage().navigateToDashboard();
	}

	@Test
	public void allDomainLinksLoaded() throws Exception {
		for ( Domain domain : expectedDomains ) {
			assertTrue( String.format( "Link to domain \'%s\' is not displayed", domain.getLinkText() ), dashboardPage.domainLinkDisplayed( domain ) );
		}

		assertEquals( String.format( "Unexpected domain links are displayed:" ), expectedDomains.size(), dashboardPage.getDomainLinksCount() );
	}

	@Test
	public void domainRecordStatisticsIsDisplayed() {
		for ( Domain domain : expectedDomains ) {
			assertEquals( String.format( "Incorrect record count for domain \'%s\':", domain.getLinkText() ), domain.getExpectedRecordsCount(), dashboardPage.getDomainRecordsCount( domain ) );
		}
	}
}