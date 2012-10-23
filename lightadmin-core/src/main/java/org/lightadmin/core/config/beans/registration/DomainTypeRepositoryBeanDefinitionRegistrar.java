package org.lightadmin.core.config.beans.registration;

import com.google.common.collect.Sets;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.beans.support.BeanNameGenerator;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.support.DynamicJpaRepositoryFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Set;

public class DomainTypeRepositoryBeanDefinitionRegistrar implements BeanDefinitionRegistrar {

	private BeanNameGenerator beanNameGenerator = BeanNameGenerator.INSTANCE;

	private final Set<DomainConfigurationInterface> domainConfigurations;

	public DomainTypeRepositoryBeanDefinitionRegistrar( final Set<DomainConfigurationInterface> domainConfigurations ) {
		this.domainConfigurations = domainConfigurations;
	}

	public DomainTypeRepositoryBeanDefinitionRegistrar( final DomainConfigurationInterface... domainConfigurations ) {
		this( Sets.newHashSet( domainConfigurations ) );
	}

	@Override
	public void registerBeanDefinitions( final BeanDefinitionRegistry registry ) {
		for ( DomainConfigurationInterface configuration : domainConfigurations ) {
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
}