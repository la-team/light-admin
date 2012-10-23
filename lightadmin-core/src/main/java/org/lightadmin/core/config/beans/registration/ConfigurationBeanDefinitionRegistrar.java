package org.lightadmin.core.config.beans.registration;

import com.google.common.collect.Sets;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.beans.support.BeanNameGenerator;
import org.lightadmin.core.config.beans.support.DomainConfigurationToBeanDefinitionTransformer;
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

	private final BeanNameGenerator beanNameGenerator = BeanNameGenerator.INSTANCE;

	private final DomainConfigurationToBeanDefinitionTransformer domainConfigurationToBeanDefinitionTransformer = DomainConfigurationToBeanDefinitionTransformer.INSTANCE;

	private final Set<DomainConfigurationInterface> domainConfigurations;

	public ConfigurationBeanDefinitionRegistrar( Set<DomainConfigurationInterface> domainConfigurations ) {
		this.domainConfigurations = domainConfigurations;
	}

	public ConfigurationBeanDefinitionRegistrar( final DomainConfigurationInterface... domainConfigurations ) {
		this( Sets.newHashSet( domainConfigurations ) );
	}

	@Override
	public void registerBeanDefinitions( final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final Map<Class, BeanReference> domainTypeConfigurations = registerDomainConfigurations( domainConfigurations, beanDefinitionRegistry );

		registerGlobalAdministrationConfiguration( beanDefinitionRegistry, domainTypeConfigurations );
	}

	private void registerGlobalAdministrationConfiguration( final BeanDefinitionRegistry beanDefinitionRegistry, final Map<Class, BeanReference> domainTypeConfigurations ) {
		BeanDefinitionBuilder builder = genericBeanDefinition( GlobalAdministrationConfiguration.class );
		builder.addPropertyValue( "domainTypeConfigurations", domainTypeConfigurations );
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

		beanDefinitionRegistry.registerBeanDefinition( beanNameGenerator.globalAdministrationConfigurationBeanName(), beanDefinition );
	}

	private Map<Class, BeanReference> registerDomainConfigurations( final Set<DomainConfigurationInterface> domainConfigurations, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final Map<Class, BeanReference> domainTypeConfigurationsBeanDefs = new ManagedMap<Class, BeanReference>();
		for ( DomainConfigurationInterface domainConfiguration : domainConfigurations ) {
			final Class<?> domainType = domainConfiguration.getDomainType();

			final String beanName = beanNameGenerator.domainTypeConfigurationBeanName( domainType );

			final BeanReference beanReference = registerDomainConfigurationBean( beanName, domainConfiguration, beanDefinitionRegistry );

			domainTypeConfigurationsBeanDefs.put( domainType, beanReference );
		}
		return domainTypeConfigurationsBeanDefs;
	}

	private BeanReference registerDomainConfigurationBean( final String beanName, final DomainConfigurationInterface domainConfiguration, final BeanDefinitionRegistry beanDefinitionRegistry ) {
		final BeanDefinition domainConfigurationBeanDefinition = domainConfigurationToBeanDefinitionTransformer.apply( domainConfiguration );
		beanDefinitionRegistry.registerBeanDefinition( beanName, domainConfigurationBeanDefinition );
		return new RuntimeBeanReference( beanName );
	}
}