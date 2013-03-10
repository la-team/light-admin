package org.lightadmin.crudOperations.create;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.page.CreatePage;
import org.lightadmin.page.ShowViewPage;

import static org.lightadmin.util.DomainAsserts.assertFieldValues;

@RunWithConfiguration( {FilterTestEntityConfiguration.class})
@LoginOnce( domain = Domain.FILTER_TEST_DOMAIN )
public class PrimitiveFields extends SeleniumIntegrationTest {

	private CreatePage createPage;
	private ShowViewPage showView;

	@Before
	public void setup() {
		createPage = getStartPage().navigateToCreatePage();
	}

	@After
	public void cleanup() {
		repopulateDatabase();
	}

	@Test
	public void itemCanBeCreatedWithAllFieldsFilledIn() {
		fillInFieldsAndSave();

		assertFieldValues(
				new String[]{ "new item", "8765", "52522", "29572.98", "90859.98", "Yes"},
				showView.getFieldValuesExcludingId() );
	}

	@Test
	public void itemCanBeCreatedWithAllFieldsEmpty() {
		leaveFieldsEmptyAndSave();

		assertFieldValues(
				new String[]{ " ", "0", "0", "0", "0", "No"},
				showView.getFieldValuesExcludingId() );
	}

	private void fillInFieldsAndSave() {
		createPage.type( "textField", "new item" );
		createPage.type( "integerField", "8765" );
		createPage.type( "primitiveIntegerField", "52522" );
		createPage.type( "decimalField", "29572.98" );
		createPage.check( "booleanField" );

		showView = createPage.submit();
	}

	private void leaveFieldsEmptyAndSave() {
		showView = createPage.submit();
	}

}

