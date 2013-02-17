package org.lightadmin.component;

import com.google.common.collect.Lists;
import org.lightadmin.SeleniumContext;
import org.lightadmin.page.EditPage;
import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataTableComponent extends StaticComponent {

	private final WebElement dataTableElement;

	private Map<String, Column> columns = new LinkedHashMap<String, Column>();

	public DataTableComponent( final WebElement dataTableElement, final SeleniumContext seleniumContext ) {
		super( seleniumContext );

		this.dataTableElement = dataTableElement;
		setColumnHeaders();
	}

	private Map<String, Column> setColumnHeaders() {
		List<String> columnNames = WebElementTransformer.transform( dataTableElement.findElements( By.xpath( "thead//th[contains(@class,'header')]/div" ) ) );

		for ( String columnName : columnNames ) {
			columns.put( columnName, new Column( columnName ) );
		}

		return columns;
	}

	public String getValueAt( int rowIndex, int columnIndex ) {
		return getCells( rowIndex ).get( columnIndex );
	}

	public List<String> getCells( int rowIndex ) {
		return WebElementTransformer.transform( cellElements( dataRowElement( rowIndex ) ) );
	}

	public QuickViewComponent showQuickViewFor( int itemId ) {
		return new QuickViewComponent( itemId, getRowForItem( itemId ), seleniumContext ).get();
	}

	public int getRowCount() {
		return dataRowElements().size();
	}

	public int getColumnCount() {
		return columns.size();
	}

	public Column getColumn( String name ) {
		return columns.get( name );
	}

	public String getColumnName( int columnIndex ) {
		return Lists.newArrayList( columns.keySet() ).get( columnIndex );
	}

	private WebElement getRowForItem( int itemId ) {
		return dataTableElement.findElement( By.xpath( "tbody/tr[td[text()=" + itemId + "]]" ) );
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

	public EditPage editItem( int itemId, String domainName ) {
		getRowForItem( itemId ).findElement( By.xpath( ".//a[@title='Edit']" ) ).click();

		return new EditPage( seleniumContext, domainName, itemId ).get();
	}

	public void deleteItemByName( String itemName ) {
		int rowId = Integer.parseInt( dataTableElement.findElement( By.xpath( "tbody/tr[td[contains(text(), '" + itemName + "')]]/td[2]" ) ).getText() );

		new DeletionDialog( getRowForItem( rowId ), seleniumContext ).confirm();
	}

	public class Column {

		private WebElement headerElement;
		private String currentSorting;

		public Column( String fieldLabel ) {
			this.headerElement = dataTableElement.findElement( By.xpath( "thead//th/div[text()=\'" + fieldLabel + "\']" ) );
		}

		public void sortDescending() {
			setCurrentSorting();

			if ( isNotSorted() ) {
				sort();
				sort();
			} else if ( isSortedAscending() ) {
				sort();
			}
		}

		public void sortAscending() {
			setCurrentSorting();

			if ( !isSortedAscending() ) {
				sort();
			}
		}

		private void sort() {
			headerElement.click();

			setCurrentSorting();
		}

		private void setCurrentSorting() {
			currentSorting = headerElement.getAttribute( "class" );
		}

		private boolean isNotSorted() {
			return currentSorting.endsWith( "sorting" );
		}

		private boolean isSortedAscending() {
			return currentSorting.endsWith( "sorting_asc" );
		}
	}
}