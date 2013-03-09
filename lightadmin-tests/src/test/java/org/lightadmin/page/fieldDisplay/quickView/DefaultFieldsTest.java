package org.lightadmin.page.fieldDisplay.quickView;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.QuickViewComponent;
import org.lightadmin.config.OrderTestEntityWithoutQuickViewFields;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.lightadmin.util.DomainAsserts.assertFieldValues;
import static org.lightadmin.util.DomainAsserts.assertQuickViewFields;

public class DefaultFieldsTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage testOrderListPage;

	@Before
	public void setup() {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( OrderTestEntityWithoutQuickViewFields.class );

		testOrderListPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_ORDERS );
	}

	@Test
	public void allPersistentFieldsAreDisplayedWhenQuickViewIsNotConfigured() {
		final QuickViewComponent quickViewComponent = testOrderListPage.showQuickViewForItem( 3 );

		String[] quickViewFieldNames = quickViewComponent.getQuickViewFieldNames();
		String[] quickViewFieldValues = quickViewComponent.getQuickViewFieldValues();

		assertQuickViewFields( new String[] {"Id:", "ShippingAddresses:", "Name:", "Customer:", "DueDate:", "LineItems:" }, quickViewFieldNames );
		assertFieldValues( new String[]{
				"3",
				" ",
				"Order3: 3 line items",
				"TestCustomer #17",
				"2010-01-01",
				"TestLineItem #101\n" +
				"TestLineItem #102\n" +
				"TestLineItem #103"
		}, quickViewFieldValues );
	}
}
