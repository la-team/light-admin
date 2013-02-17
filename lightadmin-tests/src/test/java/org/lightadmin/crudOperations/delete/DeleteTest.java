package org.lightadmin.crudOperations.delete;

import org.junit.*;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.CustomerTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.lightadmin.util.DomainAsserts.assertScopeCount;
import static org.lightadmin.util.DomainAsserts.assertTableData;

public class DeleteTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage listViewPage;

	private WebElement deletedItem;
	@Before
	public void setup() {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( CustomerTestEntityConfiguration.class );

		listViewPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_CUSTOMERS );

		applyFilter();
	}

	@After
	public void cleanup(){
		repopulateDatabase();
	}

	//Covers LA-28: Search result is not re-filtered after item deletion (https://github.com/max-dev/light-admin/issues/28)
	@Test
	public void deletedItemNotShownOnListView() {
		listViewPage.deleteItemByName( DELETED_ITEM_NAME );

		assertItemIsNotShown();
		assertFilteringIsNotResetAfterDeletion();
		assertScopeIsRecalculatedAfterDeletion();
	}

	private void applyFilter() {
		listViewPage.openAdvancedSearch();
		listViewPage.filter( FILTER_FIELD, FILTER_VALUE );

		deletedItem = webDriver().findElement( By.xpath( "//*[contains(text(),'" + DELETED_ITEM_NAME + "')]" ) );
	}

	private void assertItemIsNotShown() {
		assertFalse( webDriver().isElementPresent( deletedItem ) );
	}

	private void assertFilteringIsNotResetAfterDeletion() {
		assertTrue( webDriver().isElementValuePresent( FILTER_FIELD, FILTER_VALUE ) );
		assertTableData( expectedResult, listViewPage.getDataTable(), webDriver(), webDriverTimeout() );
	}

	private void assertScopeIsRecalculatedAfterDeletion() {
		assertScopeCount( "Deletion Test", 1, listViewPage );
	}

	public static final String DELETED_ITEM_NAME = "To delete";
	public static final String FILTER_FIELD = "lastname";
	public static final String FILTER_VALUE = "delete";

	private static final String[][] expectedResult = { { "27", "Test", "Not to delete", "notTo@delete.com" } };
}
