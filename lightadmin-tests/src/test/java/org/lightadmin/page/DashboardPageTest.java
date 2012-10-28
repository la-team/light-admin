package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class DashboardPageTest extends SeleniumIntegrationTest {

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
	public void domainLinksLoaded() throws Exception {
		assertTrue( dashboardPage.domainLinkDisplayed( Domain.PRODUCTS ) );
	}

	@Test
	public void dashboardBreadcrumbPresent() throws Exception {
		assertTrue( dashboardPage.dashboardBreadcrumbItemPresent() );
	}
}