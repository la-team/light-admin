package org.lightadmin.page;

import org.lightadmin.data.Domain;
import org.lightadmin.wrapper.Table;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class ListViewPage extends BasePage<ListViewPage> {

	private Domain domain;

	@FindBy( xpath = "//table[@id='listViewTable']" )
	private WebElement listViewTable;

	private Table table;

	@Autowired
	public ListViewPage( WebDriver webDriver, URL baseUrl ) {
		super( webDriver, baseUrl );
	}

	public ListViewPage( WebDriver webDriver, URL baseUrl, Domain domain  ) {
		this( webDriver, baseUrl );

		this.domain = domain;
	}

	public Table getTable() {
		if ( table != null ) {
			return table;
		}
		this.table = new Table( listViewTable );
		table.excludeColumns( 0, 4 );
		table.initialize();
		return table;
	}

	public int  getTableColumns() {
		return getTable().getColumns();
	}

	public int getTableRows() {
		return getTable().getRows();
	}

	public String getValueAt( int row, int column ) {
		return getTable().getValueAt( row, column );
	}

	@Override
	protected void loadPage() {
		webDriver.get( baseUrl.toString() + "/domain/" + domain.getDomainTypeName() );
	}

	@Override
	protected void isLoaded() throws Error {
	}
}