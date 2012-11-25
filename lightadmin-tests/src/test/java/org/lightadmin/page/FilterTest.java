package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

public class FilterTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage productListViewPage;

	@Before
	public void setup() {
		registerDomainTypeAdministrationConfiguration( FilterTestEntityConfiguration.class );

		productListViewPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.FILTER_TEST_DOMAIN );
	}

	@After
	public void tearDown() throws Exception {
		removeDomainTypeAdministrationConfiguration( FilterTestEntityConfiguration.class );
	}

	@Test
	public void canFilterByIntegerField() {
		productListViewPage.filter( "integerField", "12345567" );

		assertTableData( expectedResult1, productListViewPage.getDataTable() );
	}

	@Test
	public void canFilterByDecimalField() {
		productListViewPage.filter( "decimalField", "1499.99" );

		assertTableData( expectedResult2, productListViewPage.getDataTable() );
	}

	@Test
	public void textFilterIsCaseSensitive() {
		productListViewPage.filter( "textField", "Case Sensitivity Test" );

		assertTableData( expectedResult4, productListViewPage.getDataTable() );
	}

	@Test
	public void canFilterByPartialTextQuery() {
		productListViewPage.filter( "textField", "query" );

		assertTableData( expectedResult5, productListViewPage.getDataTable() );
	}


	@Test
	public void canFilterByTextWithSpecialCharacters() {
		productListViewPage.filter( "textField", "#<,&«$''(*@×¢¤₤€¥ª ™®© Аб/Cd ØøÅåÆæĈę123 ¦_{~>½" );

		assertTableData( expectedResult3, productListViewPage.getDataTable() );
	}

	private static final String[][] expectedResult1 = {{"integer search test", "1234567", "22.2"}};
	private static final String[][] expectedResult2 = {{"decimal search test", "456", "1499.99"}};
	private static final String[][] expectedResult3 = {{"#<,&«$'(*@×¢¤₤€¥ª ™®© Аб/Cd ØøÅåÆæĈę123 ¦_{~>½", "789", "22.2"}};
	private static final String[][] expectedResult4 = {{"Case Sensitivity Test", "901", "22.2"}};
	private static final String[][] expectedResult5 = {
		{"partial querysearch test", "345", "22.2"},
		{"query partial search test", "234", "22.2"},
		{"search test by partial query", "567", "22.2"}
	};
}