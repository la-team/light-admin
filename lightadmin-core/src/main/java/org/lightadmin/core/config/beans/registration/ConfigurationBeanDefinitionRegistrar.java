package org.lightadmin.core.config.beans.registration;

import com.google.common.collect.Sets;
import org.lightadmin.core.config.beans.parsing.DslConfigurationClass;
import org.lightadmin.core.config.beans.support.BeanNameGenerator;
import org.lightadmin.core.config.beans.support.ConfigurationClassToBeanDefinitionTransformer;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;

import java.util.Map;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

public class ConfigurationBeanDefinitionRegistrar implements BeanDefinitionRegistrar {

	private BeanNameGenerator beanNameGenerator = BeanNameGenerator.INSTANCE;

	private ConfigurationClassToBeanDefinitionTransformer configurationClassToBeanDefinitionTransformer = ConfigurationClassToBeanDefinitionTransformer.INSTANCE;

	private final Set<DslConfigurationClass> configurations;

	public ConfigurationBeanDefinitionRegistrar( Set<DslConfigurationClass> configurations ) {
		this.configurations = configurations;
	}

	public ConfigurationBeanDefinitionRegistrar( final DslConfigurationClass... configurationClasses ) {
		this( Sets.newHashSet( configurationClasses ) );
	}

	@Override
	public void registerBeanDefinitions( final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final Map<Class, BeanReference> domainTypeConfigurations = registerDomainConfigurations( configurations, beanDefinitionRegistry );

		registerGlobalAdministrationConfiguration( beanDefinitionRegistry, domainTypeConfigurations );
	}

	private void registerGlobalAdministrationConfiguration( final BeanDefinitionRegistry beanDefinitionRegistry, final Map<Class, BeanReference> domainTypeConfigurations ) {
		BeanDefinitionBuilder builder = genericBeanDefinition( GlobalAdministrationConfiguration.class );
		builder.addPropertyValue( "domainTypeConfigurations", domainTypeConfigurations );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

		beanDefinitionRegistry.registerBeanDefinition( beanNameGenerator.globalAdministrationConfigurationBeanName(), beanDefinition );
	}

	private Map<Class, BeanReference> registerDomainConfigurations( final Set<DslConfigurationClass> configurationsClasses, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final Map<Class, BeanReference> domainTypeConfigurations = new ManagedMap<Class, BeanReference>();
		for ( DslConfigurationClass configurationClass : configurationsClasses ) {
			final Class<?> domainType = configurationClass.getDomainType();

			final String beanName = beanNameGenerator.domainTypeConfigurationBeanName( domainType );

			final BeanReference beanReference = registerDomainConfigurationBean( beanName, configurationClass, beanDefinitionRegistry );

			domainTypeConfigurations.put( domainType, beanReference );
		}
		return domainTypeConfigurations;
	}

	private BeanReference registerDomainConfigurationBean( final String beanName, final DslConfigurationClass configurationClass, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final BeanDefinition domainConfigurationBeanDefinition = configurationClassToBeanDefinitionTransformer.apply( configurationClass );
		beanDefinitionRegistry.registerBeanDefinition( beanName, domainConfigurationBeanDefinition );
		return new RuntimeBeanReference( beanName );
	}

	public void setBeanNameGenerator( final BeanNameGenerator beanNameGenerator ) {
		this.beanNameGenerator = beanNameGenerator;
	}

	public void setConfigurationClassToBeanDefinitionTransformer( final ConfigurationClassToBeanDefinitionTransformer configurationClassToBeanDefinitionTransformer ) {
		this.configurationClassToBeanDefinitionTransformer = configurationClassToBeanDefinitionTransformer;
	}
}