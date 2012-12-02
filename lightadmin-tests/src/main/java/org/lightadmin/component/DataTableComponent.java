package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DataTableComponent extends BaseComponent {

	private final WebElement dataTableElement;

	public DataTableComponent( final WebElement dataTableElement, final SeleniumContext seleniumContext ) {
		super( seleniumContext );

		this.dataTableElement = dataTableElement;
	}

	public List<String> getColumnNames() {
		return WebElementTransformer.transform( dataTableElement.findElements( By.xpath( "thead//th[contains(@class,'header')]" ) ) );
	}

	public int getColumnCount() {
		return getColumnNames().size();
	}

	public int getRowCount() {
		return dataRowElements().size();
	}

	public String getColumnName( int columnIndex ) {
		return getColumnNames().get( columnIndex );
	}

	public String getValueAt( int rowIndex, int columnIndex ) {
		return getCells( rowIndex ).get( columnIndex );
	}

	public List<String> getCells( int rowIndex ) {
		return WebElementTransformer.transform( cellElements( dataRowElement( rowIndex ) ) );
	}

	public QuickViewComponent showQuickViewFor( int itemId ) {
		return new QuickViewComponent( itemId, dataTableElement, seleniumContext ).get();
	}

	private WebElement dataRowElement( int rowIndex ) {
		return dataRowElements().get( rowIndex );
	}

	private List<WebElement> dataRowElements() {
		return dataTableElement.findElements( By.xpath( "tbody/tr[td[contains(@class, 'data-cell')]]" ) );
	}

	private List<WebElement> cellElements( WebElement rowElement ) {
		return rowElement.findElements( By.xpath( "td[contains(@class,'data-cell')]" ) );
	}
}