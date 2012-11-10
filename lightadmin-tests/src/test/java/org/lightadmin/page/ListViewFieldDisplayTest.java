package org.lightadmin.page;

import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.ChildEntityConfiguration;
import org.lightadmin.core.reporting.ConfigurationProblemException;

public class ListViewFieldDisplayTest extends SeleniumIntegrationTest {

	@Test( expected = ConfigurationProblemException.class )
	public void referenceFieldIsNotDisplayed() {
		registerDomainTypeAdministrationConfiguration( ChildEntityConfiguration.class );
	}
}
