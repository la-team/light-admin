package org.lightadmin.core.config.beans.support;

import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.util.Transformer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.util.Assert;

public class DomainConfigurationToBeanDefinitionTransformer implements Transformer<DomainConfigurationInterface, BeanDefinition> {

	public static final DomainConfigurationToBeanDefinitionTransformer INSTANCE = new DomainConfigurationToBeanDefinitionTransformer();

	private final BeanNameGenerator beanNameGenerator = BeanNameGenerator.INSTANCE;

	public DomainConfigurationToBeanDefinitionTransformer() {
	}

	@Override
	public BeanDefinition apply( final DomainConfigurationInterface domainConfiguration ) {
		Assert.notNull( domainConfiguration );

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition( DomainTypeAdministrationConfiguration.class );

		builder.addConstructorArgValue( domainConfiguration.getDomainType() );
		builder.addConstructorArgReference( beanNameGenerator.repositoryBeanName( domainConfiguration.getDomainType() ) );

		builder.addPropertyValue( "domainTypeEntityMetadata", domainConfiguration.getDomainTypeEntityMetadata() );

		builder.addPropertyValue( "entityConfiguration", domainConfiguration.getConfiguration() );

		builder.addPropertyValue( "screenContext", domainConfiguration.getScreenContext() );

		builder.addPropertyValue( "listViewFragment", domainConfiguration.getListViewFragment() );

		builder.addPropertyValue( "scopes", domainConfiguration.getScopes() );

		builder.addPropertyValue( "filters", domainConfiguration.getFilters() );

		return builder.getBeanDefinition();
	}
}