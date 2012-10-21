package org.lightadmin.core.config.beans;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.ClassUtils;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.DomainEntityEmptyConfiguration;

public class AdministrationConfigBeanRegistryPostProcessorTest {

	private AdministrationConfigBeanRegistryPostProcessor subject;

	@Test
	public void postProcessBeanDefinitionRegistryCallDelegatedToBeanDefinitionRegistrar() throws Exception {
		subject = new AdministrationConfigBeanRegistryPostProcessor();

		BeanDefinitionRegistry beanDefinitionRegistry = new DefaultListableBeanFactory();

		final BeanDefinitionRegistrar beanDefinitionRegistrar = EasyMock.createMock( BeanDefinitionRegistrar.class );
		beanDefinitionRegistrar.registerBeanDefinitions( beanDefinitionRegistry );
		EasyMock.expectLastCall().once();

		EasyMock.replay( beanDefinitionRegistrar );

		subject.setBeanDefinitionRegistrar( beanDefinitionRegistrar );

		subject.postProcessBeanDefinitionRegistry( beanDefinitionRegistry );

		EasyMock.verify( beanDefinitionRegistrar );
	}

	@Test
	public void correctCompositeNestedRegistrarsCreated() throws Exception {
		subject = new AdministrationConfigBeanRegistryPostProcessor( DomainEntityEmptyConfiguration.class );

		final BeanDefinitionRegistrar beanDefinitionRegistrar = subject.getBeanDefinitionRegistrar();

		assertTrue ( ClassUtils.isAssignableValue( CompositeBeanDefinitionRegistrar.class, beanDefinitionRegistrar ) );

		assertCorrectNestedRegistrarsCreated( ( CompositeBeanDefinitionRegistrar ) beanDefinitionRegistrar );
	}

	private void assertCorrectNestedRegistrarsCreated( final CompositeBeanDefinitionRegistrar compositeBeanDefinitionRegistrar ) {
		final List<BeanDefinitionRegistrar> beanDefinitionRegistrars = compositeBeanDefinitionRegistrar.getBeanDefinitionRegistrars();

		assertTrue ( ClassUtils.isAssignableValue( DomainTypeRepositoryBeanDefinitionRegistrar.class, beanDefinitionRegistrars.get( 0 ) ) );
		assertTrue ( ClassUtils.isAssignableValue( ConfigurationBeanDefinitionRegistrar.class, beanDefinitionRegistrars.get( 1) ) );
		assertTrue ( ClassUtils.isAssignableValue( ConfigurationBeanPostProcessorRegistrar.class, beanDefinitionRegistrars.get( 2 ) ) );
	}

}