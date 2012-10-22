package org.lightadmin.core.config.beans.support;

import org.lightadmin.core.config.beans.parsing.DslConfigurationClass;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.util.Transformer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.Assert;

public class ConfigurationClassToBeanDefinitionTransformer implements Transformer<DslConfigurationClass, BeanDefinition> {

	public static final ConfigurationClassToBeanDefinitionTransformer INSTANCE = new ConfigurationClassToBeanDefinitionTransformer();

	private final BeanNameGenerator beanNameGenerator = BeanNameGenerator.INSTANCE;

	public ConfigurationClassToBeanDefinitionTransformer() {
	}

	@Override
	public BeanDefinition apply( final DslConfigurationClass dslConfiguration ) {
		Assert.notNull( dslConfiguration );

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition( DomainTypeAdministrationConfiguration.class );

		builder.addConstructorArgValue( dslConfiguration.getDomainType() );
		builder.addConstructorArgReference( beanNameGenerator.repositoryBeanName( dslConfiguration.getDomainType() ) );

		builder.addPropertyValue( "domainTypeEntityMetadata", dslConfiguration.getDomainTypeEntityMetadata() );

		builder.addPropertyValue( "entityConfiguration", dslConfiguration.getConfiguration() );

		builder.addPropertyValue( "screenContext", dslConfiguration.getScreenContext() );

		builder.addPropertyValue( "listViewFragment", dslConfiguration.getListViewFragment() );

		builder.addPropertyValue( "scopes", dslConfiguration.getScopes() );

		builder.addPropertyValue( "filters", dslConfiguration.getFilters() );

		return builder.getBeanDefinition();
	}
}