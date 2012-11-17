package org.lightadmin.core.config.bootstrap.scanning;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.core.config.bootstrap.scanning.config.CorrectAdministrationConfig;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AdministrationClassScannerTest {

	private AdministrationClassScanner testee;

	@Before
	public void setUp() throws Exception {
		testee = new AdministrationClassScanner();
	}

	@Test
	public void onlyAnnotatedConfigurationClassFound() throws Exception {
		final Set<Class> configClasses = testee.scan( "org.lightadmin.core.config.bootstrap.scanning.config" );

		assertEquals( 1, configClasses.size() );
		assertEquals( CorrectAdministrationConfig.class, configClasses.iterator().next() );
	}
}