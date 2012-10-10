package containers;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private WebElement table;

    private List<WebElement> tableRows;
    private List<WebElement> tableHeaders;
    private List<Integer> excludedColumns = new ArrayList<Integer>();

    private List< List<String> > tableData = new ArrayList< List<String> >();

    public Table( WebElement table ) {
        this.table = table;
    }

    public void initialize() {
        tableHeaders = table.findElements( By.tagName( "th" ) );
        tableRows = table.findElements( By.xpath( "tbody/tr" ) );
        initializeData();
    }

    public void excludeColumns( int... ids ) {
        for ( int id : ids ){
            excludedColumns.add( id );
        }
    }

    public int getRows() {
        return tableRows.size();
    }

    public int getColumns() {
        return tableHeaders.size() - excludedColumns.size();
    }

    public String getValueAt(int row, int column) {
        return tableData.get( row ).get( column );
    }

    private void initializeData() {

        for ( WebElement row : tableRows ) {

            List <String> rowData = new ArrayList<String>();
            List<WebElement> rowCells = getRowCells( row );

            for ( WebElement cell : rowCells) {
                if ( !excludedColumns.contains( rowCells.indexOf( cell ) ) )  {
                    rowData.add( cell.getText() );
                }
            }

            tableData.add( rowData );
        }
    }

    private List<WebElement> getRowCells(WebElement row) {
        return row.findElements( By.tagName( "td" ) );
    }
}

