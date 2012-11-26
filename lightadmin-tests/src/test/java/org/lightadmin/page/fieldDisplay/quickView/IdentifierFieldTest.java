package org.lightadmin.page.fieldDisplay.quickView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.QuickViewComponent;
import org.lightadmin.config.SimpleOrderTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertArrayEquals;

public class IdentifierFieldTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage testOrderListPage;

	@Before
	public void setup() {
		registerDomainTypeAdministrationConfiguration( SimpleOrderTestEntityConfiguration.class );

		testOrderListPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_ORDERS );
	}

	@After
	public void tearDown() {
		removeDomainTypeAdministrationConfiguration( SimpleOrderTestEntityConfiguration.class );
	}

	//Covers LA-17: https://github.com/max-dev/light-admin/issues/17#issuecomment-10700503
	@Test
	public void defaultIdentifierFieldIsHidden() {
		final QuickViewComponent quickViewComponent = testOrderListPage.showQuickViewForItem( 1 );

		final String[] actualFieldNames = quickViewComponent.getQuickViewFieldNames();

		assertQuickViewFields( new String[] {"Order Id", "Name"}, actualFieldNames );
	}

	private void assertQuickViewFields( String[] expectedFields, String[] actualFields ) {
		assertArrayEquals( "Missing fields on Quick View", expectedFields, actualFields );
	}
}
