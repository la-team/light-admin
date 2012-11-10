package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.demo.config.FilterTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

public class FilterTest extends SeleniumIntegrationTest {

    @Autowired
    private LoginPage loginPage;

    private ListViewPage productListViewPage;
    private String[][] expectedResult1 = {{"integer search test", "1234567", "22.2"}};
    private String[][] expectedResult2 = {{"decimal search test", "456", "1499.99"}};
    private String[][] expectedResult3 = {{"#<,&«$'(*@×¢¤₤€¥ª ™®© Аб/Cd ØøÅåÆæĈę123 ¦_{~>½", "789", "22.2"}};

    @Before
    public void setup() {
		productListViewPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_DOMAIN );
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
	public void canFilterByTextWithSpecialCharacters() {
		productListViewPage.filter( "textField", "#<,&«$''(*@×¢¤₤€¥ª ™®© Аб/Cd ØøÅåÆæĈę123 ¦_{~>½" );

		assertTableData( expectedResult3, productListViewPage.getDataTable() );
	}

}
