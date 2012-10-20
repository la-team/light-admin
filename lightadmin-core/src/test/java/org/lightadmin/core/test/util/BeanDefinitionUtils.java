package org.lightadmin.core.test.util;

import org.springframework.beans.factory.config.BeanDefinition;

public abstract class BeanDefinitionUtils {

	public static Object propertyValue( BeanDefinition beanDefinition, final String propertyName ) {
		return beanDefinition.getPropertyValues().getPropertyValue( propertyName ).getValue();
	}

	public static Object constructorArgValue( BeanDefinition beanDefinition, int index ) {
		return beanDefinition.getConstructorArgumentValues().getIndexedArgumentValue( index, Object.class ).getValue();
	}

}