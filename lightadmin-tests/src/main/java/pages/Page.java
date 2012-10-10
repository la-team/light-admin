package pages;

import data.Domain;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class Page {

    private WebDriver driver;

    @FindBy(className = "menu-sidenav")
    private WebElement navigationMenuContainer;

    public Page( WebDriver driver ) {
        this.driver = driver;
        PageFactory.initElements( driver, this );
    }

    public WebDriver getDriver() {
        return driver;
    }

    public class NavigationMenu extends Page {

        private NavigationMenu( WebDriver driver ) {
            super( driver );
        }

        public void navigateToDomain( Domain domain ) {
            navigationMenuContainer.findElement( By.linkText( domain.getLinkText() ) ).click();
        }
    }

    public NavigationMenu getNavigationMenu(){
        return new NavigationMenu( driver );
    }
}
