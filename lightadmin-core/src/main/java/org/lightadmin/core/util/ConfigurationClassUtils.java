package org.lightadmin.core.util;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.beans.parsing.DslConfigurationUnit;
import org.lightadmin.core.config.domain.support.Builder;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.ReflectionUtils.invokeMethod;

public abstract class ConfigurationClassUtils {

	public static Class<?> configurationDomainType( Class clazz ) {
		final Administration annotation = findAnnotation( clazz, Administration.class );

		return annotation == null ? null : annotation.value();
	}

	public static boolean isConfigurationCandidate( Class clazz ) {
		return findAnnotation( clazz, Administration.class ) != null;
	}


	public static boolean isConfigurationUnitDefined( final Class<?> configurationClass, DslConfigurationUnit configurationUnit, Class<? extends Builder> builderInterface ) {
		return ClassUtils.getMethodIfAvailable( configurationClass, configurationUnit.getName(), builderInterface ) != null;
	}

	public static boolean isNotConfigurationUnitDefined( final Class<?> configurationClass, DslConfigurationUnit configurationUnit, Class<? extends Builder> builderInterface ) {
		return !isConfigurationUnitDefined( configurationClass, configurationUnit, builderInterface );
	}

	@SuppressWarnings( {"unchecked"} )
	public static <T> T initializeConfigurationUnitWithBuilder( final Class<?> configurationClass, DslConfigurationUnit configurationUnit, Class<? extends Builder<T>> builderInterface, Class<? extends Builder<T>> concreteBuilderClass ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, configurationUnit.getName(), builderInterface );

		final Builder<T> builder = BeanUtils.instantiateClass( concreteBuilderClass );
		if ( method != null ) {
			try {
				return ( T ) invokeMethod( method, null, builder );
			} catch ( Exception ex ) {
				return BeanUtils.instantiateClass( concreteBuilderClass ).build();
			}
		}

		return builder.build();
	}
}