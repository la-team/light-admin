package org.lightadmin.core.config.beans.scanning;

import org.lightadmin.core.annotation.Administration;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.util.DomainConfigurationUtils.isConfigurationCandidate;

public class ConfigurationClassProvider implements ClassProvider {

	private final ClassPathScanningCandidateComponentProvider provider;

	public ConfigurationClassProvider() {
		this.provider = new ClassPathScanningCandidateComponentProvider( false );
		this.provider.addIncludeFilter( new AnnotationTypeFilter( Administration.class ) );
	}

	@Override
	public Set<Class> findClassCandidates( Set<Class> classes ) {
		Set<Class> configurations = newLinkedHashSet();
		for ( Class clazz : classes ) {
			if ( isConfigurationCandidate( clazz ) ) {
				configurations.add( clazz );
			}
		}
		return configurations;
	}

	@Override
	public Set<Class> findClassCandidates( String basePackage ) {
		final Set<BeanDefinition> candidateComponents = provider.findCandidateComponents( basePackage );
		Set<Class> configurations = newLinkedHashSet();
		for ( BeanDefinition candidateComponent : candidateComponents ) {
			configurations.add( configurationClass( ( AnnotatedBeanDefinition ) candidateComponent ) );
		}
		return configurations;
	}

	private Class configurationClass( final AnnotatedBeanDefinition definition ) {
		final String configurationClassName = definition.getMetadata().getClassName();
		return ClassUtils.resolveClassName( configurationClassName, ClassUtils.getDefaultClassLoader() );
	}
}