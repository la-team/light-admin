package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;
import static org.lightadmin.util.DomainAsserts.assertTableData;

public class BreadcrumbsTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private DashboardPage dashboardPage;

	private ListViewPage listViewPage;

	@Before
	public void setup() throws Exception {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( FilterTestEntityConfiguration.class );

		dashboardPage = loginPage.get().loginAs( User.ADMINISTRATOR );
		listViewPage = dashboardPage.navigateToDomain( Domain.FILTER_TEST_DOMAIN );
	}

	@After
	public void tearDown() throws Exception {
		listViewPage.logout();
	}

	@Test
	public void dashboardBreadcrumbPresentOnListView() throws Exception {
		assertTrue( listViewPage.dashboardBreadcrumbItemLinkPresent() );
	}

	//TODO: iko: to add more tests
}
