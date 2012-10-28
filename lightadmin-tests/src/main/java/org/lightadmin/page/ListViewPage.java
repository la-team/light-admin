package org.lightadmin.page;

import org.lightadmin.component.DataTableComponent;
import org.lightadmin.data.Domain;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;

public class ListViewPage extends SecuredPage<ListViewPage> {

	private Domain domain;

	@FindBy( xpath = "//table[@id='listViewTable']" )
	private WebElement listViewTable;

	public ListViewPage( WebDriver webDriver, URL baseUrl, Domain domain  ) {
		super( webDriver, baseUrl );

		this.domain = domain;
	}

	public DataTableComponent getDataTable() {
		return new DataTableComponent( listViewTable );
	}

	@Override
	protected void load() {
		webDriver.get( baseUrl.toString() + "/domain/" + domain.getDomainTypeName() );
	}

	@Override
	protected void isLoaded() throws Error {
	}
}