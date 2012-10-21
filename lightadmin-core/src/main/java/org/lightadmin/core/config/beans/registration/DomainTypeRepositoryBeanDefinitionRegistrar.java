package org.lightadmin.core.config.beans.registration;

import com.google.common.collect.Sets;
import org.lightadmin.core.config.beans.parsing.DslConfigurationClass;
import org.lightadmin.core.config.beans.support.BeanNameGenerator;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.support.DynamicJpaRepositoryFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Set;

public class DomainTypeRepositoryBeanDefinitionRegistrar implements BeanDefinitionRegistrar {

	private BeanNameGenerator beanNameGenerator = BeanNameGenerator.INSTANCE;

	private final Set<DslConfigurationClass> configurations;

	public DomainTypeRepositoryBeanDefinitionRegistrar( final Set<DslConfigurationClass> configurationClasses ) {
		this.configurations = configurationClasses;
	}

	public DomainTypeRepositoryBeanDefinitionRegistrar( final DslConfigurationClass... configurationClasses ) {
		this( Sets.newHashSet( configurationClasses ) );
	}

	@Override
	public void registerBeanDefinitions( final BeanDefinitionRegistry registry ) {
		for ( DslConfigurationClass configuration : configurations ) {
			registerDomainRepository( configuration.getDomainType(), registry );
		}
	}

	private String registerDomainRepository( final Class<?> domainType, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final String repositoryBeanName = beanNameGenerator.repositoryBeanName( domainType );

		final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition( DynamicJpaRepositoryFactoryBean.class );
		builder.addConstructorArgValue( domainType );
		builder.addPropertyValue( "repositoryInterface", DynamicJpaRepository.class );

		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		beanDefinitionRegistry.registerBeanDefinition( repositoryBeanName, beanDefinition );

		return repositoryBeanName;
	}

	public void setBeanNameGenerator( final BeanNameGenerator beanNameGenerator ) {
		this.beanNameGenerator = beanNameGenerator;
	}
}