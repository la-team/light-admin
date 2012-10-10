package pages;

import containers.Table;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.testng.Assert.assertEquals;


public class ProductPage extends Page {

    @FindBy( id = "productTable" )
    private WebElement table;

    private Table productTable;

    public ProductPage( WebDriver driver ) {
        super( driver );
        productTable = new Table( table );
        productTable.excludeColumns( 0, 4 );
    }

    public void verifyAllProductsAreShown( String[][] expectedProductList ) {

        productTable.initialize();

        for ( int row = 0; row < productTable.getRows(); row ++ ){
            for ( int column = 0; column < productTable.getColumns(); column++ ) {
                assertEquals( productTable.getValueAt(row, column), expectedProductList[row][column],
                        String.format( "Row: %d, column: %d: ", row + 1, column + 1 ) );
            }
        }
    }

}
