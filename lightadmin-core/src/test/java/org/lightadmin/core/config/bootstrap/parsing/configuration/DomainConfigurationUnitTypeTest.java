package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DomainConfigurationUnitTypeTest {

	@Test( expected = IllegalArgumentException.class )
	public void undefinedConfigurationUnit() {
		DomainConfigurationUnitType.forName( "Undefined configuration unit" );
	}

	@Test
	public void findConfigurationUnitByName() throws Exception {
		assertEquals( DomainConfigurationUnitType.SCREEN_CONTEXT, DomainConfigurationUnitType.forName( "screenContext" ) );
	}
}