package org.lightadmin.page;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class FilteringScopedResultTest extends SeleniumIntegrationTest {

	public static final String SELLERS_SCOPE = "Sellers";

	@Autowired
	private LoginPage loginPage;

	private ListViewPage customerListViewPage;

	@Before
	public void setup() {
		customerListViewPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.CUSTOMERS );
	}

	//todo: make tests run in fixed order
	@Test
	public void customersAreFilteredByScope() {
		customerListViewPage.selectScope( SELLERS_SCOPE );

		assertScopeIsApplied( expectedScopedCustomers, SELLERS_SCOPE );
	}

	@Test
	public void resettingFilterDoesNotResetScope() {
		customerListViewPage.selectScope( SELLERS_SCOPE );
		assertScopeIsApplied( expectedScopedCustomers, SELLERS_SCOPE );

		customerListViewPage.filter( "lastname", "Matthews1" );
		assertTableData( expectedFilteredAndScopedCustomers, customerListViewPage.getDataTable() );

		customerListViewPage.resetFilter();
		assertScopeIsApplied( expectedScopedCustomers, SELLERS_SCOPE );
	}

	@Test
	public void scopeIsAppliedToFilteredCustomers() {
		customerListViewPage.filter( "lastname", "Matthews1" );
		assertTableData( expectedFilteredCustomers, customerListViewPage.getDataTable() );

		customerListViewPage.selectScope( SELLERS_SCOPE );
		assertScopeIsApplied( expectedFilteredAndScopedCustomers, SELLERS_SCOPE );
	}

	private void assertScopeIsApplied( String[][] expectedData, String scope ) {
		assertTableData( expectedData, customerListViewPage.getDataTable() );

		assertTrue( "Selected scope is not highlighted", customerListViewPage.scopeIsHighlighted( scope ) );
	}

	private static final String[][] expectedFilteredAndScopedCustomers = {{"Dave", "Matthews1", "dave@dmband1.com"}};

	private static final String[][] expectedFilteredCustomers = {
		{"Boyd", "Matthews1", "boyd@dmband25.com"}, {"Dave", "Matthews1", "dave@dmband1.com"}
	};

	private static final String[][] expectedScopedCustomers = new String[][] {
		{"Dave", "Matthews1", "dave@dmband1.com"}, {"Dave", "Matthews2", "dave@dmband4.com"},
		{"Dave", "Matthews3", "dave@dmband7.com"}, {"Dave", "Matthews4", "dave@dmband10.com"},
		{"Dave", "Matthews5", "dave@dmband13.com"}, {"Dave", "Matthews6", "dave@dmband16.com"},
		{"Dave", "Matthews7", "dave@dmband19.com"}, {"Dave", "Matthews8", "dave@dmband22.com"}
	};
}