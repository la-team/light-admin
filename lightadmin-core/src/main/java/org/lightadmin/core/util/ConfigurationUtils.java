package org.lightadmin.core.util;

import org.lightadmin.core.annotation.Administration;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public abstract class ConfigurationUtils {

	public static Class configurationDomainType( final Class<?> configurationClass ) {
		return configurationClass.getAnnotation( Administration.class ).value();
	}

	public static Set<Class> findAdministrationConfigurations( final String basePackage ) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider( false );
		provider.addIncludeFilter( new AnnotationTypeFilter( Administration.class ) );
		final Set<BeanDefinition> candidateComponents = provider.findCandidateComponents( basePackage );

		Set<Class> configurations = newLinkedHashSet();
		for ( BeanDefinition candidateComponent : candidateComponents ) {
			final Class configurationClass = configurationClass( ( AnnotatedBeanDefinition ) candidateComponent );
			configurations.add( configurationClass );
		}

		return configurations;
	}

	private static Class configurationClass( final AnnotatedBeanDefinition definition ) {
		final String configurationClassName = definition.getMetadata().getClassName();
		return ClassUtils.resolveClassName( configurationClassName, ClassUtils.getDefaultClassLoader() );
	}
}