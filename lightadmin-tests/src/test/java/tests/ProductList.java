package tests;

import data.Domain;
import data.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ProductPage;

public class ProductList extends BaseTest {

    private static ProductPage productsPage;

    String[][] expectedProductList = {
            {"Dock", "Dock for iPhone/iPad", "49"},
            {"MacBook Pro", "Apple notebook", "1299"},
            {"iPad", "Apple tablet device", "499"}
    };

    @BeforeClass
    public void setUp(){
        productsPage = new ProductPage(getDriver());
    }

    @BeforeMethod
    public void openLoginPage() {
        getLoginPage().open();
    }

    @Test
    public void allProductsAreDisplayedForAdmin(){
        //given
        getLoginPage().logInAs( User.admin );

        //when
        getDashboard().getNavigationMenu().navigateToDomain( Domain.PRODUCTS );

        //then
        productsPage.verifyAllProductsAreShown( expectedProductList );
    }
}
