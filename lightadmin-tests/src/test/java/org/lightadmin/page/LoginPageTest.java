package org.lightadmin.page;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//TODO: iko: add test for remembering user login/pwd
@IfProfileValue( name = "security.enabled", value = "true" )
public class LoginPageTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	@Before
	public void setup() throws Exception {
		loginPage.get();
	}

	@Test
	public void administratorIsRedirectedToDashboard() {
		final DashboardPage dashboardPage = loginPage.loginAs( User.ADMINISTRATOR );

		assertTrue( dashboardPage.isLoggedIn() );
		assertTrue( dashboardPage.isDashboardPageLoaded() );

		final LoginPage actualLogoutPage = dashboardPage.logout();

		assertTrue( actualLogoutPage.isLoggedOut() );
	}

	@Test
	public void validationMessageIsDisplayedForInvalidLogin() {
		final LoginPage returnedLoginPage = loginPage.loginAsExpectingError( User.INVALID_USER );

		assertEquals( "Your login attempt was not successful, try again.\n" + "Reason: Bad credentials.", returnedLoginPage.errorMessage() );
	}
}