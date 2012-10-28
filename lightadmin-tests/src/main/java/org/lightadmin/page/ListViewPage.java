package org.lightadmin.page;

import org.lightadmin.component.DataTableComponent;
import org.lightadmin.component.FilterFormComponent;
import org.lightadmin.data.Domain;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;

public class ListViewPage extends SecuredPage<ListViewPage> {

	private Domain domain;

	@FindBy( xpath = "//table[@id='listViewTable']" )
	private WebElement listViewTable;

    private WebElement scope;

    private FilterFormComponent filterFormComponent;

    public ListViewPage( WebDriver webDriver, URL baseUrl, Domain domain  ) {
		super( webDriver, baseUrl );

        filterFormComponent = new FilterFormComponent( webDriver, baseUrl );
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

    public void selectScope( String scopeLabel ) {
        getScope( scopeLabel ).click();
    }

    public boolean scopeIsHighlighted( String scopeLabel ) {
        try {
            return getScope( scopeLabel ).getAttribute( "class" ).contains( "label-success" );
        } catch ( NoSuchElementException e ) {
            return false;
        }
    }

    public void resetFilter() {
        filterFormComponent.resetFilter();
    }

    public void filterBy( String filterField, String filterValue ) {
        filterFormComponent.filter( filterField, filterValue );
    }

    private WebElement getScope( String scopeLabel ) {
        return webDriver.findElement( By.linkText( scopeLabel ) );
    }
}