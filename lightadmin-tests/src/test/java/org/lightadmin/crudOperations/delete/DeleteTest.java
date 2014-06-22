package org.lightadmin.crudOperations.delete;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.CustomerTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.lightadmin.util.DomainAsserts.assertScopeCount;
import static org.lightadmin.util.DomainAsserts.assertTableData;

@RunWithConfiguration({CustomerTestEntityConfiguration.class})
@LoginOnce(domain = Domain.TEST_CUSTOMERS)
public class DeleteTest extends SeleniumIntegrationTest {

    private WebElement deletedItem;

    @Before
    public void setup() {
        applyFilter();
    }

    @After
    public void cleanup() {
        repopulateDatabase();
    }

    //Covers LA-28: Search result is not re-filtered after item deletion (https://github.com/max-dev/light-admin/issues/28)
    @Test
    public void deletedItemNotShownOnListView() {
        getStartPage().deleteItemByName(DELETED_ITEM_NAME);

        assertItemIsNotShown();
        assertFilteringIsNotResetAfterDeletion();
        assertScopeIsRecalculatedAfterDeletion();
    }

    private void applyFilter() {
        getStartPage().openAdvancedSearch();
        getStartPage().filter(FILTER_FIELD, FILTER_VALUE);

        deletedItem = webDriver().findElement(By.xpath("//*[contains(text(),'" + DELETED_ITEM_NAME + "')]"));
    }

    private void assertItemIsNotShown() {
        assertFalse(webDriver().isElementPresent(deletedItem));
    }

    private void assertFilteringIsNotResetAfterDeletion() {
        assertTrue(webDriver().isElementValuePresent(FILTER_FIELD, FILTER_VALUE));
        assertTableData(expectedResult, getStartPage().getDataTable(), webDriver(), webDriverTimeout());
    }

    private void assertScopeIsRecalculatedAfterDeletion() {
        assertScopeCount("DeletionTest", 1, getStartPage());
    }

    public static final String DELETED_ITEM_NAME = "To delete";
    public static final String FILTER_FIELD = "lastname";
    public static final String FILTER_VALUE = "delete";

    private static final String[][] expectedResult = {{"27", "Test", "Not to delete", "notTo@delete.com"}};
}
