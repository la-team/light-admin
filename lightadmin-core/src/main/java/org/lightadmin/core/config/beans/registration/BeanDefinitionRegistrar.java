package org.lightadmin.core.config.beans.registration;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public interface BeanDefinitionRegistrar {

	public void registerBeanDefinitions( BeanDefinitionRegistry registry );

}