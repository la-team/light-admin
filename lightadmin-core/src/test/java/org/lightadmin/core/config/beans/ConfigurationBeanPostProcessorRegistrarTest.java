package org.lightadmin.core.config.beans;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

import static org.junit.Assert.assertEquals;

public class ConfigurationBeanPostProcessorRegistrarTest {

	private static final String GLOBAL_ADMINISTRATION_CONFIGURATION_POST_PROCESSOR_BEAN_NAME = "globalAdministrationConfigurationPostProcessor";

	private ConfigurationBeanPostProcessorRegistrar subject;

	@Before
	public void setup() {
		subject = new ConfigurationBeanPostProcessorRegistrar();
		subject.setBeanNameGenerator( mockBeanNameGenerator() );
	}

	@Test
	public void globalAdministrationConfigurationPostProcessorBeanDefinitionRegistered() throws Exception {
		final Capture<BeanDefinition> beanDefinitionCapture = new Capture<BeanDefinition>();

		final BeanDefinitionRegistry beanDefinitionRegistry = EasyMock.createMock( BeanDefinitionRegistry.class );

		beanDefinitionRegistry.registerBeanDefinition( EasyMock.eq( GLOBAL_ADMINISTRATION_CONFIGURATION_POST_PROCESSOR_BEAN_NAME ), EasyMock.<BeanDefinition>capture( beanDefinitionCapture ) );
		EasyMock.expectLastCall();

		EasyMock.replay( beanDefinitionRegistry );

		subject.registerBeanDefinitions( beanDefinitionRegistry );

		EasyMock.verify( beanDefinitionRegistry );

		final BeanDefinition beanDefinition = beanDefinitionCapture.getValue();

		assertEquals( GlobalAdministrationConfigurationPostProcessor.class.getName(), beanDefinition.getBeanClassName() );
	}

	private BeanNameGenerator mockBeanNameGenerator() {
		BeanNameGenerator beanNameGenerator = EasyMock.createMock( BeanNameGenerator.class );
		EasyMock.expect( beanNameGenerator.generateBeanName( EasyMock.<BeanDefinition>anyObject(), EasyMock.<BeanDefinitionRegistry>anyObject() ) ).andReturn( GLOBAL_ADMINISTRATION_CONFIGURATION_POST_PROCESSOR_BEAN_NAME );
		EasyMock.replay( beanNameGenerator );
		return beanNameGenerator;
	}
}