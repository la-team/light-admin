package org.lightadmin.page.fieldDisplay.quickView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.QuickViewComponent;
import org.lightadmin.config.OrderTestEntityWithDefaultId;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

import static org.lightadmin.util.DomainAsserts.assertQuickViewFieldValues;
import static org.lightadmin.util.DomainAsserts.assertQuickViewFields;

public class DisplayTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage testOrderListPage;

	@Before
	public void setup() {
		registerDomainTypeAdministrationConfiguration( OrderTestEntityWithDefaultId.class );

		testOrderListPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_ORDERS );
	}

	@After
	public void tearDown() {
		removeDomainTypeAdministrationConfiguration( OrderTestEntityWithDefaultId.class );
	}

	@Test
	public void canBeHidden() {
		final QuickViewComponent quickViewComponent = testOrderListPage.showQuickViewForItem( 1 );

		quickViewComponent.hide();

		assertTrue( quickViewComponent.isHidden() );
	}

	// TODO: max: test should pass when LA-20 is implemented
	@Test
	public void correctInfoIsDisplayedAfterSorting() {
		testOrderListPage.getDataTable().getColumn( "Name" ).sortDescending();

		final QuickViewComponent quickViewComponent = testOrderListPage.showQuickViewForItem( 1 );
		final String[] actualFieldValues = quickViewComponent.getQuickViewFieldValues();

		assertQuickViewFields( new String[]{ "1", "Order1: 100 line items" }, actualFieldValues );
	}

	// TODO: max: test should pass when LA-20 is implemented
	@Test
	public void infoCanBeDisplayedForMultipleItems() {
		final QuickViewComponent quickViewComponent1 = testOrderListPage.showQuickViewForItem( 1 );
		final String[] actualFieldValues1 = quickViewComponent1.getQuickViewFieldValues();

		final QuickViewComponent quickViewComponent2 = testOrderListPage.showQuickViewForItem( 3 );
		final String[] actualFieldValues2 = quickViewComponent2.getQuickViewFieldValues();

		assertQuickViewFieldValues( new String[]{ "1", "62100" }, actualFieldValues1 );
		assertQuickViewFieldValues( new String[]{ "3", "226308" }, actualFieldValues2 );
	}

}
