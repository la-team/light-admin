package org.lightadmin.core.config.beans.support;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeanNameGeneratorTest {

	private BeanNameGenerator subject = BeanNameGenerator.INSTANCE;

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

	private static class DomainEntity {
	}
}