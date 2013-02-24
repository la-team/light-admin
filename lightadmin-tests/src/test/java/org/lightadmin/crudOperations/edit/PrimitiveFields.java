package org.lightadmin.crudOperations.edit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.FilterTestEntityConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.FieldValueBuilder;
import org.lightadmin.data.User;
import org.lightadmin.page.EditPage;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.lightadmin.page.ShowViewPage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.lightadmin.util.DomainAsserts.assertFieldValue;
import static org.lightadmin.util.DomainAsserts.assertShowViewFieldValues;

public class PrimitiveFields extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;
	private ListViewPage listViewPage;
	private EditPage editPage;

	private ShowViewPage showView;

	@Before
	public void setup() {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( FilterTestEntityConfiguration.class );

		listViewPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.FILTER_TEST_DOMAIN );
	}

	@After
	public void cleanup() {
		repopulateDatabase();
	}

	//Covers LA-34: EditView: numeric fields cannot be cleared (https://github.com/max-dev/light-admin/issues/34)
	@Test
	public void canBeCleared() {
		clearAllFieldValuesAndSave();

		assertShowViewFieldValues( clearedFields, showView );
		assertFieldValue( "calculatedField", "0", webDriver() );
		assertFieldValue( "booleanField", "No", webDriver() );
	}

	@Test
	public void canBeModified() {
		modifyFieldValuesAndSave();

		assertShowViewFieldValues( modifiedFields, showView );
		assertFieldValue( "calculatedField", "5789.52", webDriver() );
		assertFieldValue( "booleanField", "Yes", webDriver() );
	}

	private void clearAllFieldValuesAndSave() {
		editPage = listViewPage.editItem( 4 );

		for ( String fieldName : clearedFields.keySet() ) {
			editPage.clear( fieldName );
		}
		editPage.check( "booleanField" );

		showView = editPage.submit();
	}

	private void modifyFieldValuesAndSave() {
		editPage = listViewPage.editItem( 3 );

		for ( String fieldName : modifiedFields.keySet() ) {
			editPage.type( fieldName, modifiedFields.get( fieldName ) );
		}
		editPage.check( "booleanField" );

		showView = editPage.submit();
	}

	private Map<String, String> clearedFields = new FieldValueBuilder()
			.add( "textField", "" )
			.add( "integerField", "0" )
			.add( "primitiveIntegerField", "0" )
			.add( "decimalField", "0" ).build();

	private Map<String, String> modifiedFields = new FieldValueBuilder()
			.add( "textField", "new text value" )
			.add( "integerField", "1234" )
			.add( "primitiveIntegerField", "4321" )
			.add( "decimalField", "234.52" ).build();

}

