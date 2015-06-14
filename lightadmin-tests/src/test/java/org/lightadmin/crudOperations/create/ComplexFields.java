package org.lightadmin.crudOperations.create;

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
import org.lightadmin.page.CreatePage;
import org.lightadmin.page.ShowViewPage;

import static org.lightadmin.util.DomainAsserts.assertFieldValues;

@RunWithConfiguration( { TestLineItemConfiguration.class,
		TestAddressConfiguration.class,
		CustomerTestEntityConfiguration.class,
		OrderTestEntityWithComplexFields.class } )
@LoginOnce( domain = Domain.TEST_ORDERS )
public class ComplexFields extends SeleniumIntegrationTest {

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

		assertFieldValues( new String[]{
				"Order Holder",
				"Marksistskaya, Moscow, Russia\n" +
						"Via Aurelia, Rome, Italy",
				"LineItem Id: 104; Product Name: Product 2",
				"432567.00",
				selectedDate },
				showView.getFieldValuesExcludingId() );
	}

	@Test
	public void itemCanBeCreatedWithAllFieldsEmpty() {
		leaveFieldsEmptyAndSave();

		assertFieldValues( new String[]{ " ", " ", " ", "0", " " }, showView.getFieldValuesExcludingId() );
	}

	@Test
	public void associatedFieldValuesCanBeFilteredByTyping() {

		createPage.searchAndSelect( "customer", "c", "New Customer" );

		createPage.searchAndSelect( "shippingAddresses", "Rome", "Rome, Italy" );
		createPage.searchAndSelect( "shippingAddresses", "Ukraine", "Kiev, Ukraine" );

		createPage.searchAndSelect( "lineItems", "55", "Product: Product 2; Amount: 1; Total: 1299.00" );
		createPage.searchAndSelect( "lineItems", "49.00", "67. Product: Product 3; Amount: 1; Total: 49.00" );

		showView = createPage.submit();

		assertFieldValues( new String[]{
				"New Customer",
				"Kreschatik, Kiev, Ukraine\n" +
						"Via Aurelia, Rome, Italy",
				"LineItem Id: 55; Product Name: Product 2\n" +
						"LineItem Id: 67; Product Name: Product 3",
				"1348.00",
				" "  }, showView.getFieldValuesExcludingId() );
	}

	private void fillInFieldsAndSave() {
		createPage.select( "customer", "Order Holder" );
		createPage.select( "shippingAddresses", "Marksistskaya, Moscow, Russia", "Via Aurelia, Rome, Italy" );
		createPage.select( "lineItems", "104. Product: Product 2; Amount: 333; Total: 432567.00" );
		selectedDate = createPage.selectDateOfCurrentMonth( "dueDate", "8" );

		showView = createPage.submit();
	}

	private void leaveFieldsEmptyAndSave() {
		showView = createPage.submit();
	}

	private String selectedDate;
}
