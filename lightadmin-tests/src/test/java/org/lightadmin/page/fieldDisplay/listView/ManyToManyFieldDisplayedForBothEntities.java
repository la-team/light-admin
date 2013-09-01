package org.lightadmin.page.fieldDisplay.listView;

import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.ListViewWithManyToManyField;
import org.lightadmin.config.ManyToManyEntityConfiguration;
import org.lightadmin.data.Domain;

import static org.lightadmin.util.DomainAsserts.assertTableData;

@RunWithConfiguration( { ListViewWithManyToManyField.class, ManyToManyEntityConfiguration.class } )
@LoginOnce( domain = Domain.TEST_CUSTOMERS )
public class ManyToManyFieldDisplayedForBothEntities extends SeleniumIntegrationTest {

	@Test
	public void canBeShownOnListView() {
		assertTableData( expectedResult, getStartPage().getDataTable(), webDriver(), webDriverTimeout() );
	}

	private String[][] expectedResult = new String[][]{
			{ "1", "Dave", "Matthews1", "TestDiscountProgram #3" },
			{ "2", "Carter", "Beauford", "TestDiscountProgram #1" },
			{ "3", "Boyd", "Tinsley", "TestDiscountProgram #2" },
			{ "4", "Dave", "Matthews2", "" },
			{ "5", "Carter", "Beauford", "" },
			{ "6", "Boyd", "Tinsley", "" },
			{ "7", "Dave", "Matthews3", "" },
			{ "8", "Carter", "Beauford", "" },
			{ "9", "Boyd", "Tinsley", "" },
			{ "10", "Dave", "Matthews4", "" }
	};
}
