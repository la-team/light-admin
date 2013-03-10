package org.lightadmin.page;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.CustomerTestEntityConfiguration;
import org.lightadmin.data.Domain;

import static org.junit.Assert.assertTrue;
import static org.lightadmin.util.DomainAsserts.assertScopeCount;
import static org.lightadmin.util.DomainAsserts.assertTableData;

@RunWithConfiguration( {CustomerTestEntityConfiguration.class })
@LoginOnce( domain = Domain.TEST_CUSTOMERS )
public class FilteringScopedResultTest extends SeleniumIntegrationTest {

	public static final String SELLERS_SCOPE = "Sellers";

	@Before
	public void refreshListView() {
		getStartPage().navigateToDomain( Domain.TEST_CUSTOMERS );
	}

	@Test
	@Ignore
	public void allResultsAreDisplayedByDefault() {
		//todo: iko: refactor test data
	}

	@Test
	public void customersAreFilteredByScope() {
		getStartPage().selectScope( SELLERS_SCOPE );

		assertScopeIsApplied( expectedScopedCustomers, SELLERS_SCOPE );
	}

	@Test
	public void resettingFilterDoesNotResetScope() {
		getStartPage().selectScope( SELLERS_SCOPE );
		assertScopeIsApplied( expectedScopedCustomers, SELLERS_SCOPE );

		getStartPage().openAdvancedSearch();
		getStartPage().filter( "lastname", "Matthews1" );
		assertTableData( expectedFilteredAndScopedCustomers, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );

		getStartPage().resetFilter();
		assertScopeIsApplied( expectedScopedCustomers, SELLERS_SCOPE );
	}


	@Test
	public void scopeIsAppliedToFilteredCustomers() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "lastname", "Matthews1" );
		assertTableData( expectedFilteredCustomers, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );

		getStartPage().selectScope( SELLERS_SCOPE );
		assertScopeIsApplied( expectedFilteredAndScopedCustomers, SELLERS_SCOPE );
	}

    @Test
	public void defaultScopeCountIsCorrect() {
		assertScopeCount( "All", 29, getStartPage() );
		assertScopeCount( "Buyers", 29, getStartPage() );
		assertScopeCount( "Sellers", 8, getStartPage() );
	}

	//Covers LA-22 comment: https://github.com/max-dev/light-admin/issues/22#issuecomment-12013074
	@Test
	public void scopeCountIsUpdatedAfterFiltering() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "lastname", "Matthews1" );

		assertScopeCount( "All", 2, getStartPage() );
		assertScopeCount( "Buyers", 2, getStartPage() );
		assertScopeCount( "Sellers", 1, getStartPage() );
	}

	//TODO: iko: add test for scope count update for CRUD operations, filter resetting

	private void assertScopeIsApplied( String[][] expectedData, String scope ) {
		assertTableData( expectedData, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );

		assertTrue( "Selected scope is not highlighted", getStartPage().scopeIsHighlighted( scope ) );
	}

	private static final String[][] expectedFilteredAndScopedCustomers = { { "1", "Dave", "Matthews1", "dave@dmband1.com" } };

	private static final String[][] expectedFilteredCustomers = {
			{ "1", "Dave", "Matthews1", "dave@dmband1.com" },
			{ "25", "Boyd", "Matthews1", "boyd@dmband25.com" }
	};

	private static final String[][] expectedScopedCustomers = new String[][]{
			{ "1", "Dave", "Matthews1", "dave@dmband1.com" },
			{ "4", "Dave", "Matthews2", "dave@dmband4.com" },
			{ "7", "Dave", "Matthews3", "dave@dmband7.com" },
			{ "10", "Dave", "Matthews4", "dave@dmband10.com" },
			{ "13", "Dave", "Matthews5", "dave@dmband13.com" },
			{ "16", "Dave", "Matthews6", "dave@dmband16.com" },
			{ "19", "Dave", "Matthews7", "dave@dmband19.com" },
			{ "22", "Dave", "Matthews8", "dave@dmband22.com" }
	};
}