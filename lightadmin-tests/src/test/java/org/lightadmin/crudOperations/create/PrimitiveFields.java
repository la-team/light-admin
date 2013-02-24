package org.lightadmin.crudOperations.create;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.FieldValueBuilder;
import org.lightadmin.data.User;
import org.lightadmin.page.CreatePage;
import org.lightadmin.page.LoginPage;
import org.lightadmin.page.ShowViewPage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.lightadmin.util.DomainAsserts.assertFieldValue;
import static org.lightadmin.util.DomainAsserts.assertShowViewFieldValues;

public class PrimitiveFields extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;
	private CreatePage createPage;

	private ShowViewPage showView;

	@Before
	public void setup() {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( FilterTestEntityConfiguration.class );

		createPage = loginPage.get().loginAs( User.ADMINISTRATOR )
				.navigateToDomain( Domain.FILTER_TEST_DOMAIN )
				.navigateToCreatePage();
	}

	@After
	public void cleanup() {
		repopulateDatabase();
	}

	@Test
	public void itemCanBeCreatedWithAllFieldsFilledIn() {
		fillInFieldsAndSave();

		assertShowViewFieldValues( filledInFields, showView );
		assertFieldValue( "calculatedField", "90859.98", webDriver() );
		assertFieldValue( "booleanField", "Yes", webDriver() );
	}

	@Test
	public void itemCanBeCreatedWithAllFieldsEmpty() {
		leaveFieldsEmptyAndSave();

		assertShowViewFieldValues( emptyFields, showView );
		assertFieldValue( "calculatedField", "0", webDriver() );
		assertFieldValue( "booleanField", "No", webDriver() );
	}

	private void fillInFieldsAndSave() {
		for ( String fieldName : filledInFields.keySet() ) {
			createPage.type( fieldName, filledInFields.get( fieldName ) );
		}
		createPage.check( "booleanField" );

		showView = createPage.submit();
	}

	private void leaveFieldsEmptyAndSave() {
		showView = createPage.submit();
	}

	private Map<String, String> emptyFields = new FieldValueBuilder()
			.add( "textField", "" )
			.add( "integerField", "0" )
			.add( "primitiveIntegerField", "0" )
			.add( "decimalField", "0" ).build();

	private Map<String, String> filledInFields = new FieldValueBuilder()
			.add( "textField", "new item" )
			.add( "integerField", "8765" )
			.add( "primitiveIntegerField", "52522" )
			.add( "decimalField", "29572.98" ).build();

}

