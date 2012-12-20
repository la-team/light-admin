package org.lightadmin.page.fieldDisplay.listView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.OrderLineItemCalculationConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.lightadmin.util.DomainAsserts.assertTableRowData;

public class CalculationInsideComplexDataTypeField extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage testOrderListPage;

	@Before
	public void setup() {
		registerDomainTypeAdministrationConfiguration( OrderLineItemCalculationConfiguration.class );

		testOrderListPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_ORDERS );
	}

	@After
	public void tearDown() {
		removeDomainTypeAdministrationConfiguration( OrderLineItemCalculationConfiguration.class );
	}

	@Test
	public void displayingLineItemTotal() {
		assertTableRowData( expectedResult1, testOrderListPage.getDataTable(), 3 );
	}

	private String[] expectedResult1 = { "3", "Order3: 3 line items", "101. Product: Product 1; Amount: 12; Total: 5988.00\n" +
			"102. Product: Product 2; Amount: 130; Total: 168870.00\n" +
			"103. Product: Product 3; Amount: 1050; Total: 51450.00" };

}
