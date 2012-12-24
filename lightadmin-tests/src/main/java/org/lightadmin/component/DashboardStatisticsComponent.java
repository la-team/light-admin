package org.lightadmin.component;

import org.lightadmin.SeleniumContext;
import org.lightadmin.data.Domain;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardStatisticsComponent extends StaticComponent {

	@FindBy( xpath = "//table[@id='dashboard-statistics']" )
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
		return statisticsTable.findElements( By.xpath( "//tr/td/a[@class='domain-link']" ) ).size();
	}

	public int getDomainRecordsCount( Domain domain ) {
		return Integer.parseInt( domainStatisticsRow( domain ).findElement( By.className( "record-count" ) ).getText() );
	}

	public int getDomainRecordsChange( Domain domain ) {
		return Integer.parseInt( domainStatisticsRow( domain ).findElement( By.className( "record-count-change" ) ).getText() );
	}

	private WebElement domainStatisticsRow( Domain domain ) {
		return statisticsTable.findElement( By.xpath( "//tr[@id='stat-row-" + domain.getLinkText() + "']" ) );
	}
}