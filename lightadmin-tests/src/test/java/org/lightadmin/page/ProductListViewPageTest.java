package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ProductListViewPageTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage listViewPage;

	@Before
	public void setup() throws Exception {
		final DashboardPage dashboardPage = loginPage.get().loginAs( User.ADMINISTRATOR );

		listViewPage = dashboardPage.navigateToDomain( Domain.PRODUCTS );
	}

	@After
	public void tearDown() throws Exception {
		listViewPage.logout();
	}

	@Test
	public void allProductsAreDisplayedForAdmin() {
		assertProductsDisplayed( expectedProducts );
	}

	private void assertProductsDisplayed( final String[][] expectedProducts ) {
        assertTableData( expectedProducts, listViewPage.getDataTable() );
    }

    private final String[][] expectedProducts = {
		{"Dock", "Dock for iPhone/iPad", "49"},
		{"MacBook Pro", "Apple notebook", "1299"},
		{"iPad", "Apple tablet device", "499"}
	};
}