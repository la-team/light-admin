package org.lightadmin.page.fieldDisplay.listView;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.OrderLineItemCalculationConfiguration;
import org.lightadmin.data.Domain;

import static org.lightadmin.util.DomainAsserts.assertTableRowData;

@RunWithConfiguration( { OrderLineItemCalculationConfiguration.class } )
@LoginOnce( domain = Domain.TEST_ORDERS )
public class CalculationInsideComplexDataTypeField extends SeleniumIntegrationTest {

	@Test
	public void displayingLineItemTotal() {
		assertTableRowData( expectedResult1, getStartPage().getDataTable(), 3 );
	}

	private String[] expectedResult1 = { "3", "Order3: 3 line items", "101. Product: Product 1; Amount: 12; Total: 5988.00\n" +
			"102. Product: Product 2; Amount: 130; Total: 168870.00\n" +
			"103. Product: Product 3; Amount: 1050; Total: 51450.00" };

}
