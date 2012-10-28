package org.lightadmin.component;

import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DataTableComponent implements Component {

	private final WebElement dataTableElement;

	public DataTableComponent( final WebElement dataTableElement ) {
		this.dataTableElement = dataTableElement;
	}

	public List<String> getColumnNames() {
		return WebElementTransformer.transform(
                dataTableElement.findElements( By.xpath( "thead//th[contains(@class,'header')]" ) ) );
	}

	public int getColumnCount() {
		return getColumnNames().size();
	}

	public int getRowCount() {
		return rowElements().size();
	}

	public String getColumnName( int columnIndex ) {
		return getColumnNames().get( columnIndex );
	}

	public String getValueAt( int rowIndex, int columnIndex ) {
		return getCells( rowIndex ).get( columnIndex );
	}

	public List<String> getCells( int rowIndex ) {
		return WebElementTransformer.transform( cellElements( rowElement( rowIndex ) ) );
	}

	private WebElement rowElement( int rowIndex ) {
		return rowElements().get( rowIndex );
	}

	private List<WebElement> rowElements() {
		return dataTableElement.findElements( By.xpath( "tbody/tr" ) );
	}

	private List<WebElement> cellElements( WebElement rowElement ) {
		return rowElement.findElements( By.xpath( "td[contains(@class,'data-cell')]" ) );
	}
}