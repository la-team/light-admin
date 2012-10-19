package tests;

import data.User;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Login extends BaseTest {

    @BeforeMethod
    public void setUp(){
        getLoginPage().open();
    }

    @Test
    public void administratorIsRedirectedToDashboard(){
        getLoginPage().logInAs( User.admin );

        getDashboard().verifyDomainsAreListed();
    }

    @Test
    public void validationMessageIsDisplayedForInvalidLogin(){
        getLoginPage().logInAs( User.invalidUser );

        getLoginPage().verifyValidationMessage();
    }

}
