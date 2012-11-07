package org.lightadmin.page;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.config.TestLineItemConfiguration;
import org.lightadmin.demo.config.AddressAdministration;

import static org.junit.Assert.assertTrue;

public class CustomConfigurationTest extends SeleniumIntegrationTest {

	@Before
	public void setUp() throws Exception {
		registerDomainTypeAdministrationConfiguration( AddressAdministration.class );
	}

	@After
	public void tearDown() throws Exception {
		removeDomainTypeAdministrationConfiguration( TestLineItemConfiguration.class );
	}

	@Test
	public void testName() throws Exception {
		assertTrue( true );
	}
}