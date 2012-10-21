package org.lightadmin.core.config.beans.support;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.DomainEntity;

public class BeanNameGeneratorTest {

	private BeanNameGenerator subject;

	@Before
	public void setup() {
		subject = new BeanNameGenerator();
	}

	@Test( expected = IllegalArgumentException.class )
	public void nullDomainTypeNotAllowed() {
		subject.repositoryServiceExporterName( null );
	}

	@Test
	public void repositoryServiceExporterNameGeneration() throws Exception {
		assertEquals( "domainEntity", subject.repositoryServiceExporterName( DomainEntity.class ) );
	}

	@Test
	public void domainTypeConfigurationBeanNameGeneration() throws Exception {
		assertEquals( "org.lightadmin.core.config.domainEntityAdministrationConfiguration", subject.domainTypeConfigurationBeanName( DomainEntity.class ) );
	}

	@Test
	public void globalAdministrationConfigurationNameGeneration() throws Exception {
		assertEquals( "org.lightadmin.core.config.globalAdministrationConfiguration", subject.globalAdministrationConfigurationBeanName() );
	}

	@Test
	public void testName() throws Exception {
		assertEquals( "org.lightadmin.core.persistence.repository.domainEntityRepository", subject.repositoryBeanName( DomainEntity.class ) );
	}
}