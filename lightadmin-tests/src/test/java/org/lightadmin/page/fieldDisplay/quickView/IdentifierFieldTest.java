package org.lightadmin.page.fieldDisplay.quickView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.SimpleOrderTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
		testOrderListPage.showQuickViewForItem( 1 );

		assertQuickViewFields( "Order Id", "Name" );
	}

	private void assertQuickViewFields( String... expectedFields ) {
		final List<String> quickViewFieldNames = testOrderListPage.getQuickViewFieldNames();

		assertEquals( "Wrong number of fields:", expectedFields.length, quickViewFieldNames.size() );

		for( String fieldName : expectedFields ){
			assertTrue( String.format( "Field Name %s is missing on Quick View:", fieldName ),
					quickViewFieldNames.contains( fieldName ) );
		}
	}
}
