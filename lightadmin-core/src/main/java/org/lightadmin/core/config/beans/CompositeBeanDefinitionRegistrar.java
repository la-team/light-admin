package org.lightadmin.core.config.beans;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CompositeBeanDefinitionRegistrar implements BeanDefinitionRegistrar {

	private List<BeanDefinitionRegistrar> beanDefinitionRegistrars;

	public CompositeBeanDefinitionRegistrar( final BeanDefinitionRegistrar... beanDefinitionRegistrars ) {
		this.beanDefinitionRegistrars = newArrayList( beanDefinitionRegistrars );
	}

	@Override
	public void registerBeanDefinitions( final BeanDefinitionRegistry registry ) {
		for ( BeanDefinitionRegistrar beanDefinitionRegistrar : beanDefinitionRegistrars ) {
			beanDefinitionRegistrar.registerBeanDefinitions( registry );
		}
	}

	public List<BeanDefinitionRegistrar> getBeanDefinitionRegistrars() {
		return newArrayList( beanDefinitionRegistrars );
	}
}