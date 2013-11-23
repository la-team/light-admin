package org.lightadmin.crudOperations.edit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.CustomerTestEntityConfiguration;
import org.lightadmin.config.OrderTestEntityWithComplexFields;
import org.lightadmin.config.TestAddressConfiguration;
import org.lightadmin.config.TestLineItemConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.page.EditPage;
import org.lightadmin.page.ShowViewPage;

import static org.lightadmin.util.DomainAsserts.assertFieldValues;

@RunWithConfiguration( {TestLineItemConfiguration.class,
		TestAddressConfiguration.class,
		CustomerTestEntityConfiguration.class,
		OrderTestEntityWithComplexFields.class})
@LoginOnce( domain = Domain.TEST_ORDERS )
public class ComplexFields extends SeleniumIntegrationTest {

	private EditPage editPage;
	private ShowViewPage showView;

	@Before
	public void refreshListView(){
		getStartPage().navigateToDomain( Domain.TEST_ORDERS );
	}

	@After
	public void cleanup() {
		repopulateDatabase();
	}

	@Test
	public void canBeCleared() {
		clearAllFieldsAndSave();

		assertFieldValues( new String[]{" ", " ", " ", "0", " "}, showView.getFieldValuesExcludingId() );
	}

	@Test
	public void selectionsCanBeReplaced() {
		replaceSelectionsAndSave();

		assertFieldValues( new String[]{ "New Customer",

				"Baker, London, United Kingdom\n" +
						"Kreschatik, Kiev, Ukraine\n" +
						"Vesterbrogade, Copenhagen, Denmark",

				"LineItem Id: 113; Product Name: Product 1\n" +
						"LineItem Id: 110; Product Name: Product 3\n" +
						"LineItem Id: 114; Product Name: Product 1",

				"19657.00",
				"2013-10-08"},
				showView.getFieldValuesExcludingId() );

	}

	@Test
	public void selectionCanBeAdded() {
		addSelectionsAndSave();

		assertFieldValues( new String[]{ "Carter",

				"Kreschatik, Kiev, Ukraine\n" +
						"Usteristrasse, Zurich, Switzerland\n" +
						"Via Aurelia, Rome, Italy",

				"LineItem Id: 108; Product Name: Product 1\n" +
						"LineItem Id: 109; Product Name: Product 2\n" +
						"LineItem Id: 110; Product Name: Product 3\n" +
						"LineItem Id: 114; Product Name: Product 1",

				"20671.00",
				" "},
				showView.getFieldValuesExcludingId() );
	}

	@Test
	public void dateFieldCanModifiedViaDatePicker() {
		modifyDateViaDatePickerAndSave();

		assertFieldValues( new String[]{"Dave",
				"Kreschatik, Kiev, Ukraine",
				"LineItem Id: 115; Product Name: Product 3",
				"588.00",
				selectedDate},
				showView.getFieldValuesExcludingId() );
	}

	@Test
	public void dateFieldCanBeModifiedManually() {
		modifyDateManuallyAndSave();

		assertFieldValues( new String[]{"Dave",
				"Kreschatik, Kiev, Ukraine",
				"LineItem Id: 116; Product Name: Product 2",
				"40269.00",
				"2014-12-14"},
				showView.getFieldValuesExcludingId() );
	}

	private void clearAllFieldsAndSave() {
		editPage = getStartPage().editItem( 4 );

		editPage.clearSelection( "customer" );
		editPage.clearSelection( "shippingAddresses" );
		editPage.clearSelection( "lineItems" );
		editPage.clear( "dueDate" );

		showView = editPage.submit();
	}

	private void addSelectionsAndSave() {
		editPage = getStartPage().editItem( 5 );

		editPage.select( "shippingAddresses", "Kreschatik, Kiev, Ukraine" );
		editPage.select( "lineItems",
				"110. Product: Product 3; Amount: 4; Total: 196.00", "114. Product: Product 1; Amount: 7; Total: 3493.00" );

		showView = editPage.submit();
	}

	private void replaceSelectionsAndSave() {
		editPage = getStartPage().editItem( 6 );
		editPage.select( "customer", "New Customer" );

		editPage.replaceFieldSelections( "shippingAddresses",
				new String[]{ "Marksistskaya, Moscow, Russia" },
				new String[]{ "Vesterbrogade, Copenhagen, Denmark", "Baker, London, United Kingdom" } );

		editPage.replaceFieldSelections( "lineItems",
				new String[]{ "111. Product: Product 3; Amount: 6; Total: 294.00",
						"112. Product: Product 2; Amount: 12; Total: 15588.00" },
				new String[]{ "110. Product: Product 3; Amount: 4; Total: 196.00",
						"114. Product: Product 1; Amount: 7; Total: 3493.00" } );

		showView = editPage.submit();
	}

	private void modifyDateViaDatePickerAndSave() {
		editPage = getStartPage().editItem( 7 );

		selectedDate = editPage.selectDateOfCurrentMonth( "dueDate", "13" );

		showView = editPage.submit();
	}


	private void modifyDateManuallyAndSave() {
		editPage = getStartPage().editItem( 8 );

		editPage.type( "dueDate", "2014-12-14" );

		showView = editPage.submit();
	}

	private String selectedDate;
}
