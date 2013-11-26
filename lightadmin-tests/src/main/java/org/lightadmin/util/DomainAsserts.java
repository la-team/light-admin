package org.lightadmin.util;

import org.apache.commons.lang3.StringUtils;
import org.lightadmin.component.DataTableComponent;
import org.lightadmin.page.ListViewPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;

import static org.junit.Assert.*;


public class DomainAsserts {

    public static void assertTableData(final String[][] expectedData, final DataTableComponent dataTable, final WebDriver webDriver, final long timeout) {
        assertTableRowCount(expectedData, dataTable, webDriver, timeout);

        for (int row = 0; row < dataTable.getRowCount(); row++) {
            assertTableRowData(expectedData[row], dataTable, row + 1);
        }
    }

    public static void assertTableRowData(final String[] expectedRowData, final DataTableComponent dataTable, final int rowId) {
        for (int column = 0; column < dataTable.getColumnCount(); column++) {
            final String expectedCellValue = StringUtils.trimToEmpty(expectedRowData[column]);
            final String actualCellValue = StringUtils.trimToEmpty(dataTable.getValueAt(rowId - 1, column));

            assertEquals(String.format("Row: %d, column: %d: ", rowId, column + 1), expectedCellValue, actualCellValue);
        }
    }

    public static void assertTableRowCount(final String[][] expectedData, final DataTableComponent dataTable, final WebDriver webDriver, final long timeout) {
        try {
            new WebDriverWait(webDriver, timeout).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver input) {
                    return expectedData.length == dataTable.getRowCount();
                }
            });
        } catch (TimeoutException e) {
            fail(String.format("Wrong row count for the table. Expected: %d, Actual: %d",
                    expectedData.length, dataTable.getRowCount()));
        }
    }

    public static void assertQuickViewFields(final String[] expectedFields, final String[] actualFields) {
        assertArrayEquals(
                String.format("Wrong fields on Quick View. Expected fields: %s, actual fields: %s",
                        Arrays.toString(expectedFields), Arrays.toString(actualFields)),
                expectedFields, actualFields);
    }

    public static void assertFieldValues(final String[] expectedValues, final String[] actualValues) {
        assertArrayEquals(
                String.format("Wrong field values. Expected field values: %s, actual field values: %s",
                        Arrays.toString(expectedValues), Arrays.toString(actualValues)),
                expectedValues, actualValues);
    }

    public static void assertScopeCount(final String scope, final int expectedCount, final ListViewPage thePage) {
        assertEquals(String.format("Wrong count for scope '%s': ", scope),
                expectedCount, thePage.getScopeCount(scope));
    }

    public static void assertFieldValue(final String fieldName, final String expectedValue, final WebDriver webDriver) {
        assertEquals(String.format("Wrong value for field '%s'", fieldName),
                expectedValue, webDriver.findElement(By.xpath("//*[contains(@name,'" + fieldName + "')]")).getText());
    }

    public static void assertImagePreviewIsDisplayed(String viewName, final WebElement webElement, final ExtendedWebDriver webDriver, final long timeout) {
        try {
            new WebDriverWait(webDriver, timeout).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver input) {
                    return webDriver.isElementPresent(webElement.findElement(By.xpath("//img[@name='picture']")));
                }
            });
        } catch (TimeoutException e) {
            fail("Image preview is not displayed on " + viewName);
        }
    }

}
