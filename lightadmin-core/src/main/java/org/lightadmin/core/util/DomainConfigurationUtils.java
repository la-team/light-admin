package org.lightadmin.core.util;

import org.lightadmin.core.annotation.Administration;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.support.ConfigurationUnit;
import org.lightadmin.core.config.domain.support.ConfigurationUnitBuilder;
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

	public static boolean isConfigurationCandidate( Object candidate ) {
		return ClassUtils.isAssignableValue( Class.class, candidate ) && isConfigurationCandidate( ( Class ) candidate );
	}

	public static boolean isConfigurationUnitDefined( final Class<?> configurationClass, DomainConfigurationUnitType configurationUnitType, Class<? extends ConfigurationUnitBuilder> builderInterface ) {
		return ClassUtils.getMethodIfAvailable( configurationClass, configurationUnitType.getName(), builderInterface ) != null;
	}

	public static boolean isNotConfigurationUnitDefined( final Class<?> configurationClass, DomainConfigurationUnitType configurationUnitType, Class<? extends ConfigurationUnitBuilder> builderInterface ) {
		return !isConfigurationUnitDefined( configurationClass, configurationUnitType, builderInterface );
	}

	@SuppressWarnings( {"unchecked"} )
	public static <T extends ConfigurationUnit> T initializeConfigurationUnitWithBuilder( final Class<?> configurationClass, DomainConfigurationUnitType configurationUnitType, Class<? extends ConfigurationUnitBuilder<T>> builderInterface, Class<? extends ConfigurationUnitBuilder<T>> concreteBuilderClass ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, configurationUnitType.getName(), builderInterface );

		ConfigurationUnitBuilder<T> builder = BeanUtils.instantiateClass( concreteBuilderClass );
		if ( method != null ) {
			try {
				return ( T ) invokeMethod( method, null, builder );
			} catch ( Exception ex ) {
				return BeanUtils.instantiateClass( concreteBuilderClass ).build();
			}
		}

		return builder.build();
	}

	@SuppressWarnings( {"unchecked"} )
	public static <T extends ConfigurationUnit> T initializeConfigurationUnitWithBuilder( final Class<?> configurationClass, DomainConfigurationUnitType configurationUnitType, Class<? extends ConfigurationUnitBuilder<T>> builderInterface, Class<? extends ConfigurationUnitBuilder<T>> concreteBuilderClass, final Class domainType ) {
		final Method method = ClassUtils.getMethodIfAvailable( configurationClass, configurationUnitType.getName(), builderInterface );

		ConfigurationUnitBuilder<T> builder = instantiateBuilder( concreteBuilderClass, domainType );
		if ( method != null ) {
			try {
				return ( T ) invokeMethod( method, null, builder );
			} catch ( Exception ex ) {
				return instantiateBuilder( concreteBuilderClass, domainType ).build();
			}
		}

		return builder.build();
	}

	private static <T extends ConfigurationUnit> ConfigurationUnitBuilder<T> instantiateBuilder( final Class<? extends ConfigurationUnitBuilder<T>> concreteBuilderClass, final Class domainType  ) {
		Constructor<? extends ConfigurationUnitBuilder<T>> constructor = ClassUtils.getConstructorIfAvailable( concreteBuilderClass, Class.class );
		if ( constructor != null ) {
			return BeanUtils.instantiateClass( constructor, domainType );
		}
		return BeanUtils.instantiateClass( concreteBuilderClass );
	}
}