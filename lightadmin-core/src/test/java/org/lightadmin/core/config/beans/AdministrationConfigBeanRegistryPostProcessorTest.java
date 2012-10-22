package org.lightadmin.core.config.beans;

import org.easymock.EasyMock;
import org.junit.Test;
import org.lightadmin.core.config.beans.registration.BeanDefinitionRegistrar;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

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

		subject.setDomainTypeEntityMetadataResolver( EasyMock.createMock( DomainTypeEntityMetadataResolver.class ) );

		subject.postProcessBeanDefinitionRegistry( beanDefinitionRegistry );

		EasyMock.verify( beanDefinitionRegistrar );
	}
}