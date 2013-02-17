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
import org.lightadmin.page.LoginPage;
import org.lightadmin.page.ShowViewPage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PrimitiveFields extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;
	private EditPage editPage;

	private ShowViewPage showView;

	@Before
	public void setup() {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( FilterTestEntityConfiguration.class );

		editPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.FILTER_TEST_DOMAIN ).editItem( ITEM_ID );
	}

	@After
	public void cleanup() {
		repopulateDatabase();
	}

	//Covers LA-34: EditView: numeric fields cannot be cleared (https://github.com/max-dev/light-admin/issues/34)
	@Test
	public void canBeCleared() {
		clearAllFieldValuesAndSave();

		verifyEditedFieldValues( clearedFields );
		verifyCalculatedFieldValue( "0" );
	}

	@Test
	public void canBeModified() {
		modifyFieldValuesAndSave();

		verifyEditedFieldValues( modifiedFields );
		verifyCalculatedFieldValue( "5789.52" );
	}

	private void clearAllFieldValuesAndSave() {
		for ( String fieldName : clearedFields.keySet() ) {
			editPage.type( fieldName, "" );
		}

		showView = editPage.submit();
	}

	private void modifyFieldValuesAndSave() {
		for ( String fieldName : modifiedFields.keySet() ) {
			editPage.type( fieldName, modifiedFields.get( fieldName ) );
		}

		showView = editPage.submit();
	}

	private void verifyEditedFieldValues( Map<String, String> expectedFieldValueMap ) {
		for ( String fieldName : expectedFieldValueMap.keySet() ) {
			assertEquals( String.format( "Wrong value for field '%s'", fieldName ),
					expectedFieldValueMap.get( fieldName ), showView.getFieldValue( fieldName ) );
		}
	}

	private void verifyCalculatedFieldValue( String expectedValue ) {
		assertEquals( "Wrong value for the calculated field",
				expectedValue, showView.getFieldValue( "calculatedField" ) );
	}

	public static final int ITEM_ID = 4;

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

