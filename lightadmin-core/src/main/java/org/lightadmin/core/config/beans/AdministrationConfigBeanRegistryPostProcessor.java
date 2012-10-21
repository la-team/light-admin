package org.lightadmin.core.config.beans;

import com.google.common.collect.Sets;
import org.lightadmin.core.util.ConfigurationUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.Assert;

import java.util.Set;

@SuppressWarnings( "unused" )
public class AdministrationConfigBeanRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private BeanDefinitionRegistrar beanDefinitionRegistrar;

	public AdministrationConfigBeanRegistryPostProcessor( final String configurationsBasePackage ) {
		this( ConfigurationUtils.findAdministrationConfigurations( configurationsBasePackage ) );
	}

	public AdministrationConfigBeanRegistryPostProcessor( final Class... configurationClasses ) {
		this( Sets.newHashSet( configurationClasses ) );
	}

	public AdministrationConfigBeanRegistryPostProcessor( final Set<Class> configurationClasses ) {
		BeanDefinitionRegistrar domainTypeRepositoryBeanDefinitionsRegistrar = new DomainTypeRepositoryBeanDefinitionRegistrar( configurationClasses );
		BeanDefinitionRegistrar configurationBeanDefinitionRegistrar = new ConfigurationBeanDefinitionRegistrar( configurationClasses );
		BeanDefinitionRegistrar configurationBeanPostProcessorRegistrar = new ConfigurationBeanPostProcessorRegistrar();

		this.beanDefinitionRegistrar = new CompositeBeanDefinitionRegistrar(
			domainTypeRepositoryBeanDefinitionsRegistrar,
			configurationBeanDefinitionRegistrar,
			configurationBeanPostProcessorRegistrar );
	}

	@Override
	public void postProcessBeanFactory( final ConfigurableListableBeanFactory beanFactory ) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry( final BeanDefinitionRegistry registry ) throws BeansException {
		beanDefinitionRegistrar.registerBeanDefinitions( registry );
	}

	public BeanDefinitionRegistrar getBeanDefinitionRegistrar() {
		return beanDefinitionRegistrar;
	}

	public void setBeanDefinitionRegistrar( final BeanDefinitionRegistrar beanDefinitionRegistrar ) {
		Assert.notNull( beanDefinitionRegistrar );

		this.beanDefinitionRegistrar = beanDefinitionRegistrar;
	}
}