package org.lightadmin.page;

import org.junit.After;
import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.data.Domain;

import static org.lightadmin.util.DomainAsserts.assertTableData;

@RunWithConfiguration( {FilterTestEntityConfiguration.class })
@LoginOnce( domain = Domain.FILTER_TEST_DOMAIN )
public class FilterTest extends SeleniumIntegrationTest {

	@After
	public void resetFilter() {
		getStartPage().resetFilter();
	}

	//Covers LA-6: https://github.com/max-dev/light-admin/issues/6
	@Test
	public void canFilterByIntegerField() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "integerField", "1234567" );

		assertTableData( expectedResult1, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	//Covers LA-25: https://github.com/max-dev/light-admin/issues/25
	@Test
	public void canFilterByPrimitiveIntegerField() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "primitiveIntegerField", "15235" );

		assertTableData( expectedResult7, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	@Test
	public void canFilterByIdField() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "id", "9" );

		assertTableData( expectedResult6, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	@Test
	public void canFilterByDecimalField() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "decimalField", "1499.99" );

		assertTableData( expectedResult2, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	@Test
	public void textFilterIsCaseInsensitive() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "textField", "CASE sensitivity TeSt" );

		assertTableData( expectedResult4, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	@Test
	public void canFilterByPartialTextQuery() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "textField", "query" );

		assertTableData( expectedResult5, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	@Test
	public void canFilterByCombinedCriteria() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "primitiveIntegerField", "31264" );
		getStartPage().filter( "decimalField", "61.12" );

		assertTableData( expectedResult8, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	//Covers LA-46: https://github.com/max-dev/light-admin/issues/46
	@Test
	public void canFilterByTextWithSpecialCharacters() {
		getStartPage().openAdvancedSearch();
		getStartPage().filter( "textField", "管#<,&«$'(*@×¢¤₤€¥ª ™®© ØøÅåÆæĈę ¦_{~>½" );

		assertTableData( expectedResult3, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	private static final String[][] expectedResult1 = {{"1", "integer search test", "1234567", "521", "22.2", ""}};
	private static final String[][] expectedResult2 = {{"2", "decimal search test", "456", "31264", "1499.99", ""}};
	private static final String[][] expectedResult3 = {{"3", "管#<,&«$'(*@×¢¤₤€¥ª ™®© ØøÅåÆæĈę ¦_{~>½", "789", "62342", "22.2", ""}};
	private static final String[][] expectedResult4 = {
            {"4", "Case Sensitivity Test", "901", "823", "22.2", "Yes"},
            {"5", "Case sensitivity test", "901", "9521", "22.2", "No"}
    };
	private static final String[][] expectedResult5 = {
		{"6", "query partial search test", "234", "9164", "22.2", "Yes"},
		{"7", "partial querysearch test", "345", "612325", "22.2", "Yes"},
		{"8", "search test by partial query", "567", "623412", "22.2", "Yes"}
	};
	private String[][] expectedResult6 = {{"9", "Id search test", "234", "2932", "21.2", "No"}};
	private String[][] expectedResult7 = {{"10", "primitive integer search test", "345", "15235", "22.2", "Yes"}};
	private String[][] expectedResult8 = {{"11", "combined criteria search test", "345", "31264", "61.12", "Yes"}};
}