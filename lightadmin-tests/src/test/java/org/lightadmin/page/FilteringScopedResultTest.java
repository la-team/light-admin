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
        customerListViewPage.selectScope( SELLERS_SCOPE );
    }

    @Test
    public void ordersAreFilteredByScope() {
        assertScopeIsApplied( SELLERS_SCOPE );
    }

    @Test
    public void resettingFilterDoesNotResetScope() {
        customerListViewPage.filterBy( "lastname", "Matthews1" );
        assertFilterIsApplied();

        customerListViewPage.resetFilter();
        assertScopeIsApplied( SELLERS_SCOPE );
    }

    private void assertFilterIsApplied() {
        assertTableData( expectedFilteredCustomers, customerListViewPage.getDataTable() );
    }

    private void assertScopeIsApplied( String scope ) {
        assertScopeIsAppliedToOrders();

        assertTrue( "Selected scope is not highlighted", customerListViewPage.scopeIsHighlighted( scope ) );
    }

    private void assertScopeIsAppliedToOrders() {
        assertTableData( expectedScopedCustomers, customerListViewPage.getDataTable() );
    }

    private String[][] expectedFilteredCustomers = {{"Dave", "Matthews1", "dave@dmband1.com" }};

    private String[][] expectedScopedCustomers = new String[][] {
            {"Dave", "Matthews1", "dave@dmband1.com"},
            {"Dave", "Matthews2", "dave@dmband4.com"},
            {"Dave", "Matthews3", "dave@dmband7.com"},
            {"Dave", "Matthews4", "dave@dmband10.com"},
            {"Dave", "Matthews5", "dave@dmband13.com"},
            {"Dave", "Matthews6", "dave@dmband16.com"},
            {"Dave", "Matthews7", "dave@dmband19.com"},
            {"Dave", "Matthews8", "dave@dmband22.com"}
    };
}
