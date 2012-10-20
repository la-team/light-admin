package org.lightadmin.core.config.beans;

import com.google.common.collect.Sets;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.beans.support.BeanNameGenerator;
import org.lightadmin.core.config.beans.support.ConfigurationClassToBeanDefinitionTransformer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedMap;

import java.util.Map;
import java.util.Set;

import static org.lightadmin.core.util.ConfigurationUtils.configurationDomainType;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

public class ConfigurationBeanDefinitionRegistrar implements BeanDefinitionRegistrar {

	private final Set<Class> configurations;

	public ConfigurationBeanDefinitionRegistrar( Set<Class> configurations ) {
		this.configurations = configurations;
	}

	public ConfigurationBeanDefinitionRegistrar( final Class... configurationClasses ) {
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

		beanDefinitionRegistry.registerBeanDefinition( BeanNameGenerator.INSTANCE.globalAdministrationConfigurationBeanName(), beanDefinition );
	}

	private Map<Class, BeanReference> registerDomainConfigurations( final Set<Class> configurationsClasses, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final Map<Class, BeanReference> domainTypeConfigurations = new ManagedMap<Class, BeanReference>();
		for ( Class configurationClass : configurationsClasses ) {
			final Class<?> domainType = configurationDomainType( configurationClass );

			final String beanName = BeanNameGenerator.INSTANCE.domainTypeConfigurationBeanName( domainType );

			final BeanReference beanReference = registerDomainConfigurationBean( beanName, configurationClass, beanDefinitionRegistry );

			domainTypeConfigurations.put( domainType, beanReference );
		}
		return domainTypeConfigurations;
	}

	private BeanReference registerDomainConfigurationBean( final String beanName, final Class<?> configurationClass, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final BeanDefinition domainConfigurationBeanDefinition = ConfigurationClassToBeanDefinitionTransformer.INSTANCE.apply( configurationClass );
		beanDefinitionRegistry.registerBeanDefinition( beanName, domainConfigurationBeanDefinition );
		return new RuntimeBeanReference( beanName );
	}
}