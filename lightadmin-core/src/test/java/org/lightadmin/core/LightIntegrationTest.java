package org.lightadmin.core;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.lightadmin.core.config.GlobalAdministrationConfiguration;
import org.lightadmin.core.test.IntegrationTest;
import org.lightadmin.core.test.LightAdminConfigurationContextLoader;
import org.lightadmin.core.test.LightAdminTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@Category( IntegrationTest.class )
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( loader = LightAdminConfigurationContextLoader.class,
					   classes = {LightAdminTestConfiguration.class} )
public class LightIntegrationTest {

	@Autowired
	private GlobalAdministrationConfiguration configuration;

	@Test
	public void someTest() {
		assertEquals( 1, configuration.getDomainTypeConfigurations().size() );
	}
}