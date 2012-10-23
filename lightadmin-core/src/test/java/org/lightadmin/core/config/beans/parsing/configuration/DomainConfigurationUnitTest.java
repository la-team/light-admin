package org.lightadmin.core.config.beans.parsing.configuration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DomainConfigurationUnitTest {

	@Test( expected = IllegalArgumentException.class )
	public void undefinedConfigurationUnit() {
		DomainConfigurationUnit.forName( "Undefined configuration unit" );
	}

	@Test
	public void findConfigurationUnitByName() throws Exception {
		assertEquals( DomainConfigurationUnit.SCREEN_CONTEXT, DomainConfigurationUnit.forName( "screenContext" ) );
	}
}