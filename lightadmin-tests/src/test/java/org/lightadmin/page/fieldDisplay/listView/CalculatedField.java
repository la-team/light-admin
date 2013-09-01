package org.lightadmin.page.fieldDisplay.listView;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.OrderTotalCalculationConfiguration;
import org.lightadmin.data.Domain;

import static org.lightadmin.util.DomainAsserts.assertTableRowData;

@RunWithConfiguration( OrderTotalCalculationConfiguration.class )
@LoginOnce( domain = Domain.TEST_ORDERS )
public class CalculatedField extends SeleniumIntegrationTest {

	@Test
	public void displayingOrderTotal() {
		assertTableRowData( expectedResult2, getStartPage().getDataTable(), 3 );
	}

	private String[] expectedResult2 = { "3", "Order3: 3 line items", "226308.00" };
}
