package org.lightadmin.core.config.beans;

import org.junit.Before;
import org.junit.Test;
import org.lightadmin.core.annotation.Administration;

public class ConfigurationBeanDefinitionRegistrarTest {

	private ConfigurationBeanDefinitionRegistrar subject;

	@Before
	public void setup() {
		subject = new ConfigurationBeanDefinitionRegistrar( DomainEntityConfiguration.class );
	}

	@Test
	public void testName() throws Exception {
	}

	private static class DomainEntity {
	}

	@Administration( DomainEntity.class )
	private static class DomainEntityConfiguration {
	}
}