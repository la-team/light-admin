package org.lightadmin;

import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.DashboardPage;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginService {

	private final boolean securityEnabled;

	@Autowired
	private LoginPage loginPage;

	@Autowired
	private DashboardPage dashboardPage;

	private ListViewPage startPage;

	public LoginService( boolean securityEnabled ) {
		this.securityEnabled = securityEnabled;
	}

	public void navigateToDomain( Domain domain ) {
		startPage = securityEnabled ?
				loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( domain ) :
				dashboardPage.get().navigateToDomain( domain );
	}

	public ListViewPage getStartPage() {
		return startPage;
	}

	public void logout() {
		if ( securityEnabled ) {
			startPage.logout();
		}
	}
}
