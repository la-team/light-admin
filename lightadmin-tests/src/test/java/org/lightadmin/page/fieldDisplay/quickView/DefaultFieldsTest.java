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

import static org.lightadmin.util.DomainAsserts.assertQuickViewFieldValues;
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
	public void allEntityFieldsAreDisplayedWhenQuickViewIsNotConfigured() {
		final QuickViewComponent quickViewComponent = testOrderListPage.showQuickViewForItem( 1 );

		String[] quickViewFieldNames = quickViewComponent.getQuickViewFieldNames();
		String[] quickViewFieldValues= quickViewComponent.getQuickViewFieldValues();

		assertQuickViewFields( new String[]{ "Id:", "OrderTotal:", "Name:", "LineItems:" }, quickViewFieldNames );
		assertQuickViewFieldValues( new String[]{ "1", "62100", "Order1: 100 line items", " " }, quickViewFieldValues );
	}
}
