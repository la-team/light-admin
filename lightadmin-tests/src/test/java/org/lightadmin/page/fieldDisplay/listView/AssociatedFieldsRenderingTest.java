package org.lightadmin.page.fieldDisplay.listView;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.OrderTestEntityConfiguration;
import org.lightadmin.config.TestAddressConfiguration;
import org.lightadmin.data.Domain;
import org.openqa.selenium.By;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.lightadmin.util.DomainAsserts.assertTableRowData;

@RunWithConfiguration( {TestAddressConfiguration.class,OrderTestEntityConfiguration.class })
@LoginOnce( domain = Domain.TEST_ORDERS )
public class AssociatedFieldsRenderingTest extends SeleniumIntegrationTest {

	@Test
	public void manyComplexDataTypeValuesAssociatedWithItem() {
		assertTableRowData( expectedResult1, getStartPage().getDataTable(), 1 );
	}

	//Covers LA-4: https://github.com/max-dev/light-admin/issues/4
	@Test
	public void noComplexDataTypeValuesAssociatedWithItem() {
		assertTableRowData( expectedResult2, getStartPage().getDataTable(), 2 );
	}

	//Covers LA-60: https://github.com/max-dev/light-admin/issues/60
	@Test
	public void linkToAssociatedEntityWithoutAdministrationIsNotShown() {
		assertFalse( "Link to the associated entity without administration is displayed",
				webDriver().isElementPresent( By.partialLinkText( "TestCustomer" ) ) );
	}

	//TODO: iko: add tests for links to One2Many, One2One fields for configurations with and without EntityNameExtractor
	@Test
	public void linkToAssociatedEntityIsShownForManyToManyField() {
		for ( String expectedItem : expectedLinks ) {
			assertTrue( String.format( "Link to the associated item '%s' is not displayed", expectedItem ),
					webDriver().isElementPresent( webDriver().findElement( By.linkText( expectedItem ) ) ) );
		}
	}

	private String[] expectedLinks = { "Lafayette, New York, United States", "Baker, London, United Kingdom" };

	private String[] expectedResult2 = { "2", "Order2: no line items", "TestCustomer #10", "Kreschatik, Kiev, Ukraine", "" };

	private String[] expectedResult1 = { "1", "Order1: 100 line items", "TestCustomer #10",
			"Lafayette, New York, United States\n" +
					"Baker, London, United Kingdom",
			"LineItem Id: 1; Product Name: Product 1\n" +
					"LineItem Id: 2; Product Name: Product 1\n" +
					"LineItem Id: 3; Product Name: Product 1\n" +
					"LineItem Id: 4; Product Name: Product 1\n" +
					"LineItem Id: 5; Product Name: Product 1\n" +
					"LineItem Id: 6; Product Name: Product 1\n" +
					"LineItem Id: 7; Product Name: Product 1\n" +
					"LineItem Id: 8; Product Name: Product 1\n" +
					"LineItem Id: 9; Product Name: Product 1\n" +
					"LineItem Id: 10; Product Name: Product 1\n" +
					"LineItem Id: 11; Product Name: Product 1\n" +
					"LineItem Id: 12; Product Name: Product 1\n" +
					"LineItem Id: 13; Product Name: Product 1\n" +
					"LineItem Id: 14; Product Name: Product 1\n" +
					"LineItem Id: 15; Product Name: Product 1\n" +
					"LineItem Id: 16; Product Name: Product 1\n" +
					"LineItem Id: 17; Product Name: Product 2\n" +
					"LineItem Id: 18; Product Name: Product 2\n" +
					"LineItem Id: 19; Product Name: Product 2\n" +
					"LineItem Id: 20; Product Name: Product 2\n" +
					"LineItem Id: 21; Product Name: Product 2\n" +
					"LineItem Id: 22; Product Name: Product 2\n" +
					"LineItem Id: 23; Product Name: Product 2\n" +
					"LineItem Id: 24; Product Name: Product 2\n" +
					"LineItem Id: 25; Product Name: Product 2\n" +
					"LineItem Id: 26; Product Name: Product 2\n" +
					"LineItem Id: 27; Product Name: Product 2\n" +
					"LineItem Id: 28; Product Name: Product 2\n" +
					"LineItem Id: 29; Product Name: Product 2\n" +
					"LineItem Id: 30; Product Name: Product 2\n" +
					"LineItem Id: 31; Product Name: Product 2\n" +
					"LineItem Id: 32; Product Name: Product 2\n" +
					"LineItem Id: 33; Product Name: Product 2\n" +
					"LineItem Id: 34; Product Name: Product 2\n" +
					"LineItem Id: 35; Product Name: Product 2\n" +
					"LineItem Id: 36; Product Name: Product 2\n" +
					"LineItem Id: 37; Product Name: Product 2\n" +
					"LineItem Id: 38; Product Name: Product 2\n" +
					"LineItem Id: 39; Product Name: Product 2\n" +
					"LineItem Id: 40; Product Name: Product 2\n" +
					"LineItem Id: 41; Product Name: Product 2\n" +
					"LineItem Id: 42; Product Name: Product 2\n" +
					"LineItem Id: 43; Product Name: Product 2\n" +
					"LineItem Id: 44; Product Name: Product 2\n" +
					"LineItem Id: 45; Product Name: Product 2\n" +
					"LineItem Id: 46; Product Name: Product 2\n" +
					"LineItem Id: 47; Product Name: Product 2\n" +
					"LineItem Id: 48; Product Name: Product 2\n" +
					"LineItem Id: 49; Product Name: Product 2\n" +
					"LineItem Id: 50; Product Name: Product 2\n" +
					"LineItem Id: 51; Product Name: Product 2\n" +
					"LineItem Id: 52; Product Name: Product 2\n" +
					"LineItem Id: 53; Product Name: Product 2\n" +
					"LineItem Id: 54; Product Name: Product 2\n" +
					"LineItem Id: 55; Product Name: Product 2\n" +
					"LineItem Id: 56; Product Name: Product 2\n" +
					"LineItem Id: 57; Product Name: Product 3\n" +
					"LineItem Id: 58; Product Name: Product 3\n" +
					"LineItem Id: 59; Product Name: Product 3\n" +
					"LineItem Id: 60; Product Name: Product 3\n" +
					"LineItem Id: 61; Product Name: Product 3\n" +
					"LineItem Id: 62; Product Name: Product 3\n" +
					"LineItem Id: 63; Product Name: Product 3\n" +
					"LineItem Id: 64; Product Name: Product 3\n" +
					"LineItem Id: 65; Product Name: Product 3\n" +
					"LineItem Id: 66; Product Name: Product 3\n" +
					"LineItem Id: 67; Product Name: Product 3\n" +
					"LineItem Id: 68; Product Name: Product 3\n" +
					"LineItem Id: 69; Product Name: Product 3\n" +
					"LineItem Id: 70; Product Name: Product 3\n" +
					"LineItem Id: 71; Product Name: Product 3\n" +
					"LineItem Id: 72; Product Name: Product 3\n" +
					"LineItem Id: 73; Product Name: Product 3\n" +
					"LineItem Id: 74; Product Name: Product 3\n" +
					"LineItem Id: 75; Product Name: Product 3\n" +
					"LineItem Id: 76; Product Name: Product 3\n" +
					"LineItem Id: 77; Product Name: Product 3\n" +
					"LineItem Id: 78; Product Name: Product 3\n" +
					"LineItem Id: 79; Product Name: Product 3\n" +
					"LineItem Id: 80; Product Name: Product 3\n" +
					"LineItem Id: 81; Product Name: Product 3\n" +
					"LineItem Id: 82; Product Name: Product 3\n" +
					"LineItem Id: 83; Product Name: Product 3\n" +
					"LineItem Id: 84; Product Name: Product 3\n" +
					"LineItem Id: 85; Product Name: Product 3\n" +
					"LineItem Id: 86; Product Name: Product 3\n" +
					"LineItem Id: 87; Product Name: Product 3\n" +
					"LineItem Id: 88; Product Name: Product 3\n" +
					"LineItem Id: 89; Product Name: Product 3\n" +
					"LineItem Id: 90; Product Name: Product 3\n" +
					"LineItem Id: 91; Product Name: Product 3\n" +
					"LineItem Id: 92; Product Name: Product 3\n" +
					"LineItem Id: 93; Product Name: Product 3\n" +
					"LineItem Id: 94; Product Name: Product 3\n" +
					"LineItem Id: 95; Product Name: Product 3\n" +
					"LineItem Id: 96; Product Name: Product 3\n" +
					"LineItem Id: 97; Product Name: Product 3\n" +
					"LineItem Id: 98; Product Name: Product 3\n" +
					"LineItem Id: 99; Product Name: Product 3\n" +
					"LineItem Id: 100; Product Name: Product 3" };
}
