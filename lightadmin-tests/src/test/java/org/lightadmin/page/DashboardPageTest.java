package org.lightadmin.page;

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

	@Before
	public void setup() throws Exception {
		loginPage.get();
	}

	@Test
	public void domainLinksLoaded() throws Exception {
		final DashboardPage dashboardPage = loginPage.loginAs( User.ADMINISTRATOR.getLogin(), User.ADMINISTRATOR.getPassword() );

		assertTrue( dashboardPage.domainLinkDisplayed( Domain.PRODUCTS ) );
	}
}