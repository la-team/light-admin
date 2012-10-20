package org.lightadmin.core.config.beans;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;

public class ConfigurationBeanPostProcessorRegistrar implements BeanDefinitionRegistrar {

	private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

	@Override
	public void registerBeanDefinitions( final BeanDefinitionRegistry registry ) {
		registerSimpleBean( GlobalAdministrationConfigurationPostProcessor.class, registry );
	}

	private void registerSimpleBean( final Class<?> beanClass, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition( beanClass ).getBeanDefinition();
		final String beanName = generateBeanName( beanDefinition, beanDefinitionRegistry );

		beanDefinitionRegistry.registerBeanDefinition( beanName, beanDefinition );
	}

	private String generateBeanName( BeanDefinition beanDefinition, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		return beanNameGenerator.generateBeanName( beanDefinition, beanDefinitionRegistry );
	}

	public void setBeanNameGenerator( final BeanNameGenerator beanNameGenerator ) {
		this.beanNameGenerator = beanNameGenerator;
	}
}