package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.lightadmin.data.Domain.*;

public class DashboardPageTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private DashboardPage dashboardPage;

	private List<Domain> expectedDomains = newLinkedList();

	@Before
	public void setup() throws Exception {
		setExpectedDomains();
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
// TODO: I'm not sure we need this 'records count change'
//			assertEquals( String.format( "Incorrect progress bar percentage for domain \'%s\':", domain.getLinkText() ), domain.getExpectedRecordsPercentage(), dashboardPage.getDomainRecordsChange( domain ) );
		}
	}

	private void setExpectedDomains() {
		expectedDomains.add( PRODUCTS.setExpectedRecordCount( 3 ) );
		expectedDomains.add( ORDERS.setExpectedRecordCount( 2 ) );
		expectedDomains.add( ADDRESSES.setExpectedRecordCount( 2 ) );
		expectedDomains.add( CUSTOMERS.setExpectedRecordCount( 25 ) );
	}
}