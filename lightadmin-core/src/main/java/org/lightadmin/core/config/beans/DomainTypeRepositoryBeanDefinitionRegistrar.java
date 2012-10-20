package org.lightadmin.core.config.beans;

import com.google.common.collect.Sets;
import org.lightadmin.core.config.beans.support.BeanNameGenerator;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.support.DynamicJpaRepositoryFactoryBean;
import org.lightadmin.core.util.ConfigurationUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Set;

public class DomainTypeRepositoryBeanDefinitionRegistrar implements BeanDefinitionRegistrar {

	private final Set<Class> configurations;

	public DomainTypeRepositoryBeanDefinitionRegistrar( final Set<Class> configurationClasses ) {
		this.configurations = configurationClasses;
	}

	public DomainTypeRepositoryBeanDefinitionRegistrar( final Class... configurationClasses ) {
		this( Sets.newHashSet( configurationClasses ) );
	}

	@Override
	public void registerBeanDefinitions( final BeanDefinitionRegistry registry ) {
		for ( Class configuration : configurations ) {
			registerDomainRepository( ConfigurationUtils.configurationDomainType( configuration ), registry );
		}
	}

	private String registerDomainRepository( final Class<?> domainType, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final String repositoryBeanName = BeanNameGenerator.INSTANCE.repositoryBeanName( domainType );

		final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition( DynamicJpaRepositoryFactoryBean.class );
		builder.addConstructorArgValue( domainType );
		builder.addPropertyValue( "repositoryInterface", DynamicJpaRepository.class );

		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		beanDefinitionRegistry.registerBeanDefinition( repositoryBeanName, beanDefinition );

		return repositoryBeanName;
	}
}