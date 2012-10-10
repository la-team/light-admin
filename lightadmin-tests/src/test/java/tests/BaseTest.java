package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import pages.Dashboard;
import pages.LoginPage;
import util.Configuration;

import java.util.concurrent.TimeUnit;

public class BaseTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static Dashboard dashboard;

    @BeforeSuite
    public void suiteSetUp(){
        driver = new FirefoxDriver();
        loginPage = new LoginPage( driver );
        dashboard = new Dashboard( driver );

        driver.manage().timeouts().implicitlyWait( Configuration.getElementWaitTime(), TimeUnit.SECONDS );

        loginPage.open();

        driver.manage().window().maximize();
    }

    @AfterSuite
    public void suiteTearDown(){
        driver.quit();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public Dashboard getDashboard(){
        return dashboard;
    }
}
