package org.lightadmin;

import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.DashboardPage;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class LoginService {

	@Value( "${security.enabled}" )
	private boolean securityEnabled;

	@Autowired
	private LoginPage loginPage;

	@Autowired
	private DashboardPage dashboardPage;

	private ListViewPage startPage;

	public void navigateToDomain( Domain domain ) {
		if ( securityEnabled ) {
			startPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( domain );
		} else {
			startPage = dashboardPage.get().navigateToDomain( domain );
		}
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
