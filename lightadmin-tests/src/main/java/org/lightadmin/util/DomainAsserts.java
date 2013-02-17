package org.lightadmin.util;

import org.apache.commons.lang.StringUtils;
import org.lightadmin.component.DataTableComponent;
import org.lightadmin.page.ListViewPage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;


public class DomainAsserts {

	public static void assertTableData( final String[][] expectedData, final DataTableComponent dataTable, final WebDriver webDriver, final long timeout ) {
		assertTableRowCount( expectedData, dataTable, webDriver, timeout );

		for ( int row = 0; row < dataTable.getRowCount(); row++ ) {
			assertTableRowData( expectedData[ row ], dataTable, row + 1 );
		}
	}

	public static void assertTableRowData( final String[] expectedRowData, final DataTableComponent dataTable, final int rowId ) {
		for ( int column = 0; column < dataTable.getColumnCount(); column++ ) {
			final String expectedCellValue = StringUtils.trimToEmpty( expectedRowData[ column ] );
			final String actualCellValue = StringUtils.trimToEmpty( dataTable.getValueAt( rowId - 1, column ) );

			assertEquals( String.format( "Row: %d, column: %d: ", rowId, column + 1 ), expectedCellValue, actualCellValue );
		}
	}

	public static void assertTableRowCount( final String[][] expectedData, final DataTableComponent dataTable, final WebDriver webDriver, final long timeout ) {
		try {
			new WebDriverWait( webDriver, timeout ).until( new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply( @Nullable WebDriver input ) {
					return expectedData.length == dataTable.getRowCount();
				}
			} );
		} catch ( TimeoutException e ) {
			fail( String.format( "Wrong row count for the table. Expected: %d, Actual: %d",
					expectedData.length, dataTable.getRowCount() ) );
		}
	}

	public static void assertQuickViewFields( String[] expectedFields, String[] actualFields ) {
		assertArrayEquals(
				String.format( "Wrong fields on Quick View. Expected fields: %s, actual fields: %s",
						Arrays.toString( expectedFields ), Arrays.toString( actualFields ) ),
				expectedFields, actualFields );
	}

	public static void assertQuickViewFieldValues( String[] expectedValues, String[] actualValues ) {
		assertArrayEquals(
				String.format( "Wrong field values on Quick View. Expected field values: %s, actual field values: %s",
						Arrays.toString( expectedValues ), Arrays.toString( actualValues ) ),
				expectedValues, actualValues );
	}

	public static void assertScopeCount( String scope, int expectedCount, ListViewPage thePage ) {
		assertEquals( String.format( "Wrong count for scope '%s': ", scope ),
				expectedCount, thePage.getScopeCount( scope ) );
	}

}
