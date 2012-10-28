package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.DataTableComponent;
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
		final DataTableComponent dataTable = listViewPage.getDataTable();

		assertProductsDisplayed( expectedProducts, dataTable );
	}

	private void assertProductsDisplayed( final String[][] expectedProducts, final DataTableComponent dataTable ) {
		for ( int row = 0; row < dataTable.getRowCount(); row++ ) {
			for ( int column = 0; column < dataTable.getColumnCount(); column++ ) {
				final String expectedCellValue = expectedProducts[row][column];
				final String actualCellValue = dataTable.getValueAt( row, column );

				assertEquals( String.format( "Row: %d, column: %d: ", row + 1, column + 1 ), expectedCellValue, actualCellValue );
			}
		}
	}

	private final String[][] expectedProducts = {
		{"Dock", "Dock for iPhone/iPad", "49"},
		{"MacBook Pro", "Apple notebook", "1299"},
		{"iPad", "Apple tablet device", "499"}
	};
}