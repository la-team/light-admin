package org.lightadmin.page.fieldDisplay.quickView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.QuickViewComponent;
import org.lightadmin.config.OrderTestEntityWithUserDefinedId;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.lightadmin.util.DomainAsserts.assertQuickViewFields;

public class UserDefinedIdentifierFieldTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage testOrderListPage;

	@Before
	public void setup() {
		registerDomainTypeAdministrationConfiguration( OrderTestEntityWithUserDefinedId.class );

		testOrderListPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_ORDERS );
	}

	@After
	public void tearDown() {
		removeDomainTypeAdministrationConfiguration( OrderTestEntityWithUserDefinedId.class );
	}

	// TODO: max: test should pass when LA-20 is implemented
	@Test
	public void defaultIdentifierFieldIsDisplayed() {
		final QuickViewComponent quickViewComponent = testOrderListPage.showQuickViewForItem( 1 );

		final String[] actualFieldNames = quickViewComponent.getQuickViewFieldNames();

		assertQuickViewFields( new String[]{ "Order Id", "Order Total" }, actualFieldNames );
	}
}
