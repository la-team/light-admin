package org.lightadmin;

import org.apache.commons.lang.StringUtils;
import org.junit.runner.RunWith;
import org.lightadmin.component.DataTableComponent;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.util.DomainConfigurationUtils;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( loader = AnnotationConfigContextLoader.class, classes = SeleniumConfig.class )
public abstract class SeleniumIntegrationTest {

	@Autowired
	private SeleniumContext seleniumContext;

	@Autowired
	private GlobalConfigurationManagementService globalConfigurationManagementService;

	protected void registerDomainTypeAdministrationConfiguration( Class configurationClass ) {
		globalConfigurationManagementService.registerDomainTypeConfiguration( ConfigurationUnitsConverter.fromConfiguration( configurationClass ) );
	}

	protected void removeDomainTypeAdministrationConfiguration( Class configurationClass ) {
		globalConfigurationManagementService.removeDomainTypeAdministrationConfiguration( DomainConfigurationUtils.configurationDomainType( configurationClass ) );
	}

	protected void removeAllDomainTypeAdministrationConfigurations() {
		globalConfigurationManagementService.removeAllDomainTypeAdministrationConfigurations();
	}

	protected WebDriver webDriver() {
		return seleniumContext.getWebDriver();
	}

	protected URL baseUrl() {
		return seleniumContext.getBaseUrl();
	}

	protected void assertTableData( final String[][] expectedData, final DataTableComponent dataTable ) {
		assertTableRowCount( expectedData, dataTable );

		for ( int row = 0; row < dataTable.getRowCount(); row++ ) {
			assertTableRowData( expectedData[ row ], dataTable, row + 1 );
		}
	}

	protected void assertTableRowData( final String[] expectedRowData, final DataTableComponent dataTable, final int rowId ) {
		for ( int column = 0; column < dataTable.getColumnCount(); column++ ) {
			final String expectedCellValue = StringUtils.trimToEmpty( expectedRowData[ column ] );
			final String actualCellValue = StringUtils.trimToEmpty( dataTable.getValueAt( rowId - 1, column ) );

			assertEquals( String.format( "Row: %d, column: %d: ", rowId, column + 1 ), expectedCellValue, actualCellValue );
		}
	}

	protected void assertQuickViewFields( String[] expectedFields, String[] actualFields ) {
		assertArrayEquals(
				String.format( "Wrong field list on Quick View. Expected fields: %s, actual fields: %s",
						Arrays.toString( expectedFields ), Arrays.toString( actualFields ) ),
				expectedFields, actualFields );
	}

	protected void assertQuickViewFieldValues( String[] expectedValues, String[] actualValues ) {
		assertArrayEquals(
				String.format( "Wrong field values on Quick View. Expected field values: %s, actual field values: %s",
						Arrays.toString( expectedValues ), Arrays.toString( actualValues ) ),
				expectedValues, actualValues );
	}

	private void assertTableRowCount( final String[][] expectedData, final DataTableComponent dataTable ) {
		try {
			new WebDriverWait( webDriver(), seleniumContext.getWebDriverWaitTimeout() ).until( new ExpectedCondition<Boolean>() {
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
}