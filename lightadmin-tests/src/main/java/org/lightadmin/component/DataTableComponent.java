package org.lightadmin.component;

import com.google.common.collect.Lists;
import org.lightadmin.SeleniumContext;
import org.lightadmin.page.EditPage;
import org.lightadmin.util.WebElementTransformer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataTableComponent extends StaticComponent {

	private final WebElement dataTableElement;

	private Map<String, Column> columns = new LinkedHashMap<>();

	public DataTableComponent( final WebElement dataTableElement, final SeleniumContext seleniumContext ) {
		super( seleniumContext );

		this.dataTableElement = dataTableElement;
		setColumnHeaders();
	}

	private Map<String, Column> setColumnHeaders() {
		List<WebElement> columnElements =
				dataTableElement.findElements(By.xpath(".//div[contains(@class, 'dataTables_scrollHeadInner')]//thead//th[contains(@class, 'header')]/div"));

		for ( WebElement columnElement : columnElements ) {
			Column column = new Column(columnElement);
			columns.put( column.getName(), column );
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

	public QuickViewComponent showQuickViewFor( String itemName ) {
		int itemId = getItemIdByName( itemName );
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
		return dataTableElement.findElement( By.xpath( ".//div[contains(@class, 'dataTables_scrollBody')]//tbody/tr[td[text()=" + itemId + "]]" ) );
	}

	public WebElement getRowForItem( String itemName ) {
		return getRowForItem( getItemIdByName( itemName ) );
	}

	private int getItemIdByName( String itemName ) {
		return Integer.parseInt( dataTableElement.findElement( By.xpath( ".//div[contains(@class, 'dataTables_scrollBody')]//tbody/tr[td[contains(text(), '" + itemName + "')]]/td[2]" ) ).getText() );
	}

	private WebElement dataRowElement( int rowIndex ) {
		return dataRowElements().get( rowIndex );
	}

	private List<WebElement> dataRowElements() {
		return dataTableElement.findElements( By.xpath( ".//div[contains(@class, 'dataTables_scrollBody')]//tbody/tr[td[contains(@class, 'data-cell')]]" ) );
	}

	private List<WebElement> cellElements( WebElement rowElement ) {
		return rowElement.findElements( By.xpath( "td[contains(@class,'data-cell')]" ) );
	}

	public EditPage editItem(int itemId, String domainName) {
		final WebElement row = getRowForItem(itemId);
		((Locatable) row).getCoordinates().inViewPort();
		row.findElement(By.xpath(".//a[@title='Edit']")).click();

		return new EditPage(seleniumContext, domainName, itemId).get();
	}

	public void deleteItemByName( String itemName ) {
		new DeletionDialog( getRowForItem( itemName ), seleniumContext ).confirm();
	}

	public class Column {

		private WebElement headerElement;
		private String currentSorting;

		public Column( WebElement headerElement ) {
			this.headerElement = headerElement;
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

		public String getName() {
			return headerElement.getText();
		}
	}
}