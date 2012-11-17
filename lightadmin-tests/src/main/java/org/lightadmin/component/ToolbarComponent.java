package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.page.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ToolbarComponent extends BaseComponent {

	@FindBy( id = "settings-menu" )
	private WebElement settingsMenu;

	@FindBy( id = "user-menu" )
	private WebElement userMenu;


	public ToolbarComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	private void openDropdownMenu( WebElement dropDownMenu ) {
		dropDownMenu.findElement( By.className( "dropdown-toggle" ) ).click();
	}

	private WebElement findLogoutLink() {
		return userMenu.findElement( By.linkText( "Logout" ) );
	}

	public LoginPage logout() {
		openDropdownMenu( userMenu );
		findLogoutLink().click();

		return new LoginPage( seleniumContext ).get();
	}

	public boolean isLoggedIn() {
		return isElementPresent( userMenu );
	}
}