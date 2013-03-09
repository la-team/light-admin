package org.lightadmin;

import org.lightadmin.data.Domain;
import org.lightadmin.data.User;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginService {

	@Autowired
	private LoginPage loginPage;

	private ListViewPage startPage;

	public void loginAndNavigateToDomain( Domain domain ) {
		startPage = loginPage.get().loginAs( User.ADMINISTRATOR ).navigateToDomain( domain );
	}

	public ListViewPage getStartPage() {
		return startPage;
	}

	public void logout() {
		startPage.logout();
	}
}
