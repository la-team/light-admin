package org.lightadmin.page;

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

	private final String[][] expectedProducts = {
		{"Dock", "Dock for iPhone/iPad", "49"}, {"MacBook Pro", "Apple notebook", "1299"},
		{"iPad", "Apple tablet device", "499"}
	};

	@Before
	public void setup() throws Exception {
		loginPage.get();
	}

	@Test
	public void allProductsAreDisplayedForAdmin() {
		final DashboardPage dashboardPage = loginPage.loginAs( User.ADMINISTRATOR.getLogin(), User.ADMINISTRATOR.getPassword() );

		final ListViewPage listViewPage = dashboardPage.navigateToDomain( Domain.PRODUCTS );

		assertProductsDisplayed( expectedProducts, listViewPage );
	}

	private void assertProductsDisplayed( final String[][] expectedProducts, final ListViewPage listViewPage ) {
		for ( int row = 0; row < listViewPage.getTableRows(); row++ ) {
			for ( int column = 0; column < listViewPage.getTableColumns(); column++ ) {
				final String expectedCellValue = expectedProducts[row][column];
				final String actualCellValue = listViewPage.getValueAt( row, column );

				assertEquals( String.format( "Row: %d, column: %d: ", row + 1, column + 1 ), expectedCellValue, actualCellValue );
			}
		}
	}
}