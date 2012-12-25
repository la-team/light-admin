package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.page.DashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BreadcrumbsComponent extends StaticComponent {

	@FindBy( xpath = "//ul[@id='breadcrumb']" )
	private WebElement breadcrumbsContainer;

	public BreadcrumbsComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public DashboardPage navigateToDashboard() {
		if ( dashboardBreadcrumbLinkPresent() ) {
			dashboardBreadcrumbLink().click();
		}

		return new DashboardPage( seleniumContext ).get();
	}

	public boolean dashboardBreadcrumbLinkPresent() {
		return breadcrumbItemLinkPresent( "Dashboard" );
	}

	private WebElement dashboardBreadcrumbLink() {
		return breadcrumbsContainer.findElement( By.linkText( "Dashboard" ) );
	}

	private boolean breadcrumbItemLinkPresent( final String breadcrumbItemLinkText ) {
		try {
			breadcrumbsContainer.findElement( By.linkText( breadcrumbItemLinkText ) );
			return true;
		} catch ( NoSuchElementException e ) {
			return false;
		}
	}
}