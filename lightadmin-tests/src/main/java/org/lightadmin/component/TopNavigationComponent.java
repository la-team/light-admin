package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.page.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TopNavigationComponent extends StaticComponent {

	@FindBy( id = "topNav" )
	private WebElement topNavigation;

	public TopNavigationComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public LoginPage logout() {
		findLogoutLink().click();

		return new LoginPage( seleniumContext ).get();
	}

	public boolean isLoggedIn() {
		return webDriver().isElementPresent( topNavigation.findElement( By.className( "welcome" ) ) );
	}

	private WebElement findLogoutLink() {
		return topNavigation.findElement( By.linkText( "Logout" ) );
	}
}