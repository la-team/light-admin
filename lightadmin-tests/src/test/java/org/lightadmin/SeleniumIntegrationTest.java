package org.lightadmin;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( loader = AnnotationConfigContextLoader.class, classes = SeleniumConfig.class )
public abstract class SeleniumIntegrationTest {

	@Autowired
	private WebDriver webDriver;

	@Autowired
	private URL baseUrl;

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
		return webDriver;
	}

	protected URL baseUrl() {
		return baseUrl;
	}

	protected void assertTableData( final String[][] expectedData, final DataTableComponent dataTable ) {
        assertTableRowCount( expectedData, dataTable );

		for ( int row = 0; row < dataTable.getRowCount(); row++ ) {
			for ( int column = 0; column < dataTable.getColumnCount(); column++ ) {
				final String expectedCellValue = expectedData[row][column];
				final String actualCellValue = dataTable.getValueAt( row, column );

				assertEquals( String.format( "Row: %d, column: %d: ", row + 1, column + 1 ), expectedCellValue, actualCellValue );
			}
		}
	}

    private void assertTableRowCount( final String[][] expectedData, final DataTableComponent dataTable ) {
        try { new WebDriverWait( webDriver(), 5 ).until( new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(@Nullable WebDriver input) {
                return expectedData.length == dataTable.getRowCount();
            } });
        } catch ( TimeoutException e ) {
           fail( String.format( "Wrong row count for the table. Expected: %d, Actual: %d",
                   expectedData.length,  dataTable.getRowCount() ) );
        }
    }
}