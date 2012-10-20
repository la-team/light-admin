package org.lightadmin.core.config.beans;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public interface BeanDefinitionRegistrar {

	public void registerBeanDefinitions( BeanDefinitionRegistry registry );

}