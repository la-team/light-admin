package org.lightadmin.crudOperations.create;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.CustomerTestEntityConfiguration;
import org.lightadmin.config.OrderTestEntityWithComplexFields;
import org.lightadmin.config.TestAddressConfiguration;
import org.lightadmin.config.TestLineItemConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.CreatePage;
import org.lightadmin.page.LoginPage;
import org.lightadmin.page.ShowViewPage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.lightadmin.util.DomainAsserts.assertFieldValues;

public class ComplexFields extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;
	private CreatePage createPage;

	private ShowViewPage showView;

	@Before
	public void setup() {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( TestLineItemConfiguration.class );
		registerDomainTypeAdministrationConfiguration( TestAddressConfiguration.class );
		registerDomainTypeAdministrationConfiguration( CustomerTestEntityConfiguration.class );
		registerDomainTypeAdministrationConfiguration( OrderTestEntityWithComplexFields.class );

		createPage = loginPage.get().loginAs( User.ADMINISTRATOR )
				.navigateToDomain( Domain.TEST_ORDERS )
				.navigateToCreatePage();
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

		String[] showViewFieldValues = showView.getFieldValuesExcludingId();

		assertFieldValues( new String[]{" ", " ", " ", "0", " "}, showViewFieldValues );
	}

	private void fillInFieldsAndSave() {
		createPage.select( "customer", "Order Holder" );
		createPage.multiSelect( "shippingAddresses", new String[]{"Marksistskaya, Moscow, Russia", "Via Aurelia, Rome, Italy"} );
		createPage.multiSelect( "lineItems", new String[]{ "104. Product: Product 2; Amount: 333; Total: 432567.00" } );
		selectedDate = createPage.selectDateOfCurrentMonth( "dueDate", "8" );

		showView = createPage.submit();
	}

	private void leaveFieldsEmptyAndSave() {
		showView = createPage.submit();
	}

	private String selectedDate;
}
