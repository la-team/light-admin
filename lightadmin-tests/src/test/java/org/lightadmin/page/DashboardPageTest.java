package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.CustomerTestEntityConfiguration;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.config.OrderTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.lightadmin.data.Domain.*;

@RunWithConfiguration( {
						   OrderTestEntityConfiguration.class, CustomerTestEntityConfiguration.class,
						   FilterTestEntityConfiguration.class
					   } )
public class DashboardPageTest extends SeleniumIntegrationTest {

	private final List<Domain> expectedDomains = newArrayList( TEST_ORDERS.setExpectedRecordCount( 3 ), TEST_CUSTOMERS.setExpectedRecordCount( 27 ), FILTER_TEST_DOMAIN.setExpectedRecordCount( 11 ) );

	@Autowired
	private LoginPage loginPage;

	private DashboardPage dashboardPage;

	@Before
	public void setup() throws Exception {
		dashboardPage = loginPage.get().loginAs( User.ADMINISTRATOR );
	}

	@After
	public void tearDown() throws Exception {
		dashboardPage.logout();
	}

	@Test
	public void allDomainLinksLoaded() throws Exception {
		for ( Domain domain : expectedDomains ) {
			assertTrue( String.format( "Link for \'%s\' is not displayed", domain.getLinkText() ), dashboardPage.domainLinkDisplayed( domain ) );
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