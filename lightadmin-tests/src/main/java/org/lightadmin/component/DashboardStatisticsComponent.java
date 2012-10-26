package org.lightadmin.component;

import org.lightadmin.data.Domain;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;

public class DashboardStatisticsComponent extends BaseComponent {

	@FindBy( xpath = "//table[@id='dashboardLinks']" )
	private WebElement statisticsTable;

	public DashboardStatisticsComponent( final WebDriver webDriver, final URL baseUrl ) {
		super( webDriver, baseUrl );
	}

	public boolean domainLinkDisplayed( Domain domain ) {
		try {
			return statisticsTable.findElement( By.linkText( domain.getLinkText() ) ).isDisplayed();
		} catch ( NoSuchElementException e ) {
			return false;
		}
	}
}