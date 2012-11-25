package org.lightadmin.page.fieldDisplay.listView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.ParentTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

public class ComplexDataTypeFieldTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage complexEntityDomainPage;

	@Before
	public void setup() {
		registerDomainTypeAdministrationConfiguration( ParentTestEntityConfiguration.class );

		complexEntityDomainPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.COMPLEX_ENTITY_TEST_DOMAIN );
	}

	@After
	public void tearDown() {
		removeDomainTypeAdministrationConfiguration( ParentTestEntityConfiguration.class);
	}

	@Test
	public void manyComplexDataTypeValuesAssociatedWithItem() {
		assertTableRowData( expectedResult1, complexEntityDomainPage.getDataTable(), 1 );
	}

	//Covers LA-4: https://github.com/max-dev/light-admin/issues/4
	@Test
	public void noComplexDataTypeValuesAssociatedWithItem() {
		assertTableRowData( expectedResult2, complexEntityDomainPage.getDataTable(), 2 );
	}

	private String[] expectedResult2 = { "2", "Parent Item2: no complex items", "" };

	private String[] expectedResult1 = { "1", "Parent Item1: 100 complex items", "Complex Entity name: Parent1.Entity 1; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 2; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 3; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 4; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 5; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 6; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 7; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 8; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 9; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 10; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 11; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 12; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 13; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 14; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 15; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 16; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 17; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 18; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 19; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 20; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 21; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 22; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 23; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 24; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 25; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 26; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 27; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 28; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 29; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 30; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 31; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 32; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 33; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 34; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 35; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 36; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 37; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 38; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 39; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 40; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 41; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 42; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 43; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 44; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 45; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 46; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 47; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 48; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 49; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 50; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 51; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 52; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 53; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 54; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 55; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 56; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 57; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 58; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 59; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 60; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 61; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 62; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 63; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 64; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 65; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 66; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 67; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 68; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 69; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 70; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 71; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 72; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 73; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 74; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 75; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 76; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 77; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 78; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 79; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 80; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 81; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 82; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 83; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 84; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 85; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 86; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 87; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 88; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 89; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 90; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 91; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 92; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 93; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 94; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 95; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 96; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 97; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 98; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 99; Child Entity name: Child Item 3\n" +
															"Complex Entity name: Parent1.Entity 100; Child Entity name: Child Item 3"};
}
