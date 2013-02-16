package org.lightadmin.page.fieldDisplay.editView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.OrderTotalCalculationConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.EditPage;
import org.lightadmin.page.LoginPage;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;

@Ignore( "Incorrect test: orderTotal field wasn't marked as @Transient" )
public class CalculatedField extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	private EditPage editPage;

	@Before
	public void setup() {
		removeAllDomainTypeAdministrationConfigurations();

		registerDomainTypeAdministrationConfiguration( OrderTotalCalculationConfiguration.class );

		editPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( Domain.TEST_ORDERS ).editItem( 1 );
	}

	//Covers LA-2 comment: https://github.com/max-dev/light-admin/issues/2#issuecomment-12477966
	@Test
	public void shouldNotBeVisible() {
		assertFalse( webDriver().findElement( By.name( "orderTotal" ) ).isDisplayed() );
	}
}