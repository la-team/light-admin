package org.lightadmin.core.util;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnit;
import org.lightadmin.core.config.domain.support.Builder;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.ReflectionUtils.invokeMethod;

public abstract class DomainConfigurationUtils {

	public static Class<?> configurationDomainType( Class clazz ) {
		final Administration annotation = findAnnotation( clazz, Administration.class );

		return annotation == null ? null : annotation.value();
	}

	public static boolean isConfigurationCandidate( Class clazz ) {
		return findAnnotation( clazz, Administration.class ) != null;
	}


	public static boolean isConfigurationUnitDefined( final Class<?> configurationClass, DomainConfigurationUnit configurationUnit, Class<? extends Builder> builderInterface ) {
		return ClassUtils.getMethodIfAvailable( configurationClass, configurationUnit.getName(), builderInterface ) != null;
	}

	public static boolean isNotConfigurationUnitDefined( final Class<?> configurationClass, DomainConfigurationUnit configurationUnit, Class<? extends Builder> builderInterface ) {
		return !isConfigurationUnitDefined( configurationClass, configurationUnit, builderInterface );
	}

	@SuppressWarnings( {"unchecked"} )
	public static <T> T initializeConfigurationUnitWithBuilder( final Class<?> configurationClass, DomainConfigurationUnit configurationUnit, Class<? extends Builder<T>> builderInterface, Class<? extends Builder<T>> concreteBuilderClass, final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, configurationUnit.getName(), builderInterface );

		Builder<T> builder = instantiateBuilder( concreteBuilderClass, domainTypeEntityMetadata );
		if ( method != null ) {
			try {
				return ( T ) invokeMethod( method, null, builder );
			} catch ( Exception ex ) {
				return instantiateBuilder( concreteBuilderClass, domainTypeEntityMetadata ).build();
			}
		}

		return builder.build();
	}

	private static <T> Builder<T> instantiateBuilder( final Class<? extends Builder<T>> concreteBuilderClass, final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		Constructor<? extends Builder<T>> constructor = ClassUtils.getConstructorIfAvailable( concreteBuilderClass, DomainTypeEntityMetadata.class );
		if ( constructor != null ) {
			return BeanUtils.instantiateClass( constructor, domainTypeEntityMetadata );
		}
		return BeanUtils.instantiateClass( concreteBuilderClass );
	}
}