package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.data.Domain;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardStatisticsComponent extends BaseComponent {

	@FindBy( xpath = "//table[@id='dashboardLinks']" )
	private WebElement statisticsTable;

	public DashboardStatisticsComponent( SeleniumContext seleniumContext ) {
		super( seleniumContext );
	}

	public boolean domainLinkDisplayed( Domain domain ) {
		try {
			return statisticsTable.findElement( By.linkText( domain.getLinkText() ) ).isDisplayed();
		} catch ( NoSuchElementException e ) {
			return false;
		}
	}

	public int getDomainLinksCount() {
		return statisticsTable.findElements( By.tagName( "a" ) ).size();
	}

	public boolean isProgressBarDisplayed( Domain domain ) {
		try {
			return progressBarForDomain( domain ).isDisplayed();
		} catch ( NoSuchElementException e ) {
			return false;
		}
	}

	public int getDomainRecordsCount( Domain domain ) {
		return Integer.parseInt( progressBarForDomain( domain ).findElement( By.className( "row-count" ) ).getText() );
	}

	public int getDomainRecordsPercentage( Domain domain ) {
		return Integer.parseInt( progressBarForDomain( domain ).getAttribute( "style" ).replaceAll( "[\\D]", "" ) );
	}

	private WebElement progressBarForDomain( Domain domain ) {
		return statisticsTable.findElement( By.xpath( "//tr[@id='stat-row-" + domain.getLinkText() + "']/td//div[@class='bar']" ) );
	}
}