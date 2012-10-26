package org.lightadmin.page;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.data.User;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginPageTest extends SeleniumIntegrationTest {

	@Autowired
	private LoginPage loginPage;

	@Before
	public void setup() throws Exception {
		loginPage.get();
	}

	@Test
	public void administratorIsRedirectedToDashboard() {
		final DashboardPage dashboardPage = loginPage.loginAs( User.ADMINISTRATOR.getLogin(), User.ADMINISTRATOR.getPassword() );

		assertTrue( dashboardPage.isDashboardPageLoaded() );
	}

	@Test
	public void validationMessageIsDisplayedForInvalidLogin() {
		final LoginPage returnedLoginPage = loginPage.loginAsExpectingError( User.INVALID_USER.getLogin(), User.INVALID_USER.getPassword() );

		assertEquals( "Your login attempt was not successful, try again.\n" + "Reason: Bad credentials.", returnedLoginPage.errorMessage() );
	}
}