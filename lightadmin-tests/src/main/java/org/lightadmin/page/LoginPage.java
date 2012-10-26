package org.lightadmin.page;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

import static org.junit.Assert.fail;

@Component
public class LoginPage extends BasePage<LoginPage> {

	@FindBy( id = "j_username" )
	private WebElement login;

	@FindBy( id = "j_password" )
	private WebElement password;

	@FindBy( className = "btn" )
	private WebElement submitButton;

	@Autowired
	public LoginPage( WebDriver webDriver, URL baseUrl ) {
		super( webDriver, baseUrl );
	}

	public LoginPage enterLogin( String login ) {
		clearAndType( this.login, login );
		return this;
	}

	public LoginPage enterPassword( String password ) {
		clearAndType( this.password, password );
		return this;
	}

	public void submit() {
		submitButton.submit();
	}

	public LoginPage logout() {
		webDriver.get( baseUrl + "/logout" );

		return new LoginPage( webDriver, baseUrl );
	}

	public DashboardPage loginAs( String login, String password ) {
		enterLogin( login ).enterPassword( password ).submit();

		return new DashboardPage( webDriver, baseUrl );
	}

	public LoginPage loginAsExpectingError( String login, String password ) {
		enterLogin( login ).enterPassword( password ).submit();

		return new LoginPage( webDriver, baseUrl );
	}

	public String errorMessage() {
		return webDriver.findElement( By.className( "alert-message" ) ).getText();
	}

	@Override
	protected void loadPage() {
		webDriver.get( baseUrl + "/login" );
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			webDriver.findElement( By.xpath( "//div[@id='login']" ) );
		} catch ( NoSuchElementException e ) {
			fail();
		}
	}
}