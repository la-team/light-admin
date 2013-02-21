package org.lightadmin.page.fieldDisplay.listView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.ListViewWithManyToManyField;
import org.lightadmin.config.ManyToManyEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.lightadmin.util.DomainAsserts.assertTableData;

@Ignore
//Displaying a field from a related entity for both inter-dependent entities will be handled in M2:
//https://github.com/max-dev/light-admin/issues/43#issuecomment-13802275
public class ManyToManyField extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage listViewPage;

	@Before
	public void setup() {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( ListViewWithManyToManyField.class );
		registerDomainTypeAdministrationConfiguration( ManyToManyEntityConfiguration.class );

		listViewPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_CUSTOMERS );
	}

	@Test
	public void canBeShownOnListView() {
		assertTableData( expectedResult, listViewPage.getDataTable(), webDriver(), webDriverTimeout() );
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
