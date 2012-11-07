package org.lightadmin;

import org.junit.runner.RunWith;
import org.lightadmin.component.DataTableComponent;
import org.lightadmin.core.config.domain.support.ConfigurationUnitsConverter;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
import org.lightadmin.core.util.DomainConfigurationUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( loader = AnnotationConfigContextLoader.class, classes = SeleniumConfig.class )
public class SeleniumIntegrationTest {

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

	protected WebDriver webDriver() {
		return webDriver;
	}

	protected URL baseUrl() {
		return baseUrl;
	}

	protected void assertTableData( final String[][] expectedData, final DataTableComponent dataTable ) {
		for ( int row = 0; row < dataTable.getRowCount(); row++ ) {
			for ( int column = 0; column < dataTable.getColumnCount(); column++ ) {
				final String expectedCellValue = expectedData[row][column];
				final String actualCellValue = dataTable.getValueAt( row, column );

				assertEquals( String.format( "Row: %d, column: %d: ", row + 1, column + 1 ), expectedCellValue, actualCellValue );
			}
		}
	}
}