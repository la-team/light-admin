package org.lightadmin.component;

import org.lightadmin.page.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;

public class ToolbarComponent extends BaseComponent {

	@FindBy( id = "settings-menu" )
	private WebElement settingsMenu;

	@FindBy( id = "user-menu" )
	private WebElement userMenu;


	private void openDropdownMenu( WebElement dropDownMenu ) {
		dropDownMenu.findElement( By.className( "dropdown-toggle" ) ).click();
	}

	private WebElement findLogoutLink() {
		return userMenu.findElement( By.linkText( "Logout" ) );
	}

	public ToolbarComponent( final WebDriver webDriver, final URL baseUrl ) {
		super( webDriver, baseUrl );
	}

	public LoginPage logout() {
		openDropdownMenu( userMenu );
		findLogoutLink().click();

		return new LoginPage( webDriver, baseUrl );
	}

	public boolean isLoggedIn() {
		return isElementPresent( userMenu );
	}
}