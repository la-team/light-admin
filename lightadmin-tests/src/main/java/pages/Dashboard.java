package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.testng.Assert.assertTrue;


public class Dashboard extends Page {

    @FindBy( xpath = "//table//td/a[@href='/lightadmin/domain/product']" )
    private WebElement productLink;

    public Dashboard( WebDriver driver ) {
        super( driver );
    }

    public void verifyDomainsAreListed() {
        assertTrue( productLink.isDisplayed() );
    }
}
