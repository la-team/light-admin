package pages;

import data.User;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import util.Configuration;

import static org.testng.Assert.assertEquals;

public class LoginPage extends Page {

    String url= Configuration.getBaseUrl() + "/lightadmin/login";

    @FindBy( id = "j_username" )
    private WebElement login;

    @FindBy( id = "j_password" )
    private WebElement password;

    @FindBy( className = "btn" )
    private WebElement submitButton;

    @FindBy( className = "alert-block" )
    private WebElement validationArea;

    public LoginPage( WebDriver driver ) {
        super( driver );
    }

    public void logInAs( User user ) {
        open();
        login.sendKeys( user.getLogin() );
        password.sendKeys( user.getPassword() );
        submitButton.submit();
    }

    //todo: get actual text without button label
    public void verifyValidationMessage() {
        assertEquals( validationArea.getText(), "×\nYour login attempt was not successful, try again.\n" +
                "Reason: Bad credentials." );
    }

    public LoginPage open() {
        getDriver().get( url );
        return this;
    }

}
