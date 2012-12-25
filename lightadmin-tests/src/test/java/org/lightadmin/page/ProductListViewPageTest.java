package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;
import static org.lightadmin.util.DomainAsserts.assertTableData;

public class ProductListViewPageTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private DashboardPage dashboardPage;

	private ListViewPage listViewPage;

	@Before
	public void setup() throws Exception {
		dashboardPage = loginPage.get().loginAs( User.ADMINISTRATOR );

		listViewPage = dashboardPage.navigateToDomain( Domain.PRODUCTS );
	}

	@After
	public void tearDown() throws Exception {
		listViewPage.logout();
	}

	@Test
	public void dashboardBreadcrumbPresent() throws Exception {
		assertTrue( dashboardPage.dashboardBreadcrumbItemLinkPresent() );
	}

	@Test
	public void allProductsAreDisplayedForAdmin() {
		assertProductsDisplayed( expectedProducts );
	}

	private void assertProductsDisplayed( final String[][] expectedProducts ) {
		assertTableData( expectedProducts, listViewPage.getDataTable(), webDriver(), webDriverTimeout() );
	}

	private static final String[][] expectedProducts = {
		{"1", "iPad", "Apple tablet device", "499.1"},
		{"2", "MacBook Pro", "Apple notebook", "1299.5"},
		{"3", "Dock", "Dock for iPhone/iPad", "49.4"}
	};
}