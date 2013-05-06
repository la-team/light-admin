package org.lightadmin.page.fieldDisplay;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.InvalidFieldAdmin;

import static org.junit.matchers.JUnitMatchers.both;
import static org.junit.matchers.JUnitMatchers.containsString;


public class InvalidFieldTest extends SeleniumIntegrationTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	//Covers LA-72 (https://github.com/max-dev/light-admin/issues/72)
	@Test
	public void correctErrorMessageIsShown() {

		expectedException.expectMessage(
				both( containsString( "Domain Configuration")).and(containsString("InvalidFieldAdmin"))
						.and( containsString( "Unit")).and( containsString( "filters"))
						.and( containsString( "Invalid property/path 'Missing Field' defined!" )));

		registerDomainTypeAdministrationConfiguration( InvalidFieldAdmin.class );
	}
}
