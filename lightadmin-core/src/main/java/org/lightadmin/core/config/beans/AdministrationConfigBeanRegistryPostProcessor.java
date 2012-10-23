package org.lightadmin.core.config.beans;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.lightadmin.core.config.beans.parsing.DomainConfigurationParser;
import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.beans.registration.BeanDefinitionRegistrar;
import org.lightadmin.core.config.beans.registration.CompositeBeanDefinitionRegistrar;
import org.lightadmin.core.config.beans.registration.ConfigurationBeanDefinitionRegistrar;
import org.lightadmin.core.config.beans.registration.DomainTypeRepositoryBeanDefinitionRegistrar;
import org.lightadmin.core.config.beans.scanning.ClassProvider;
import org.lightadmin.core.config.beans.scanning.ConfigurationClassProvider;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadataResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.Set;

public class AdministrationConfigBeanRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private BeanDefinitionRegistrar beanDefinitionRegistrar;

	private ClassProvider configurationClassProvider;

	private DomainConfigurationParser configurationParser;

	private DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> domainTypeEntityMetadataResolver;

	private Set<Class> configurationClasses;

	private String configurationClassesBasePackage;

	private AdministrationConfigBeanRegistryPostProcessor( final String configurationsBasePackage, final Set<Class> configurationClasses ) {
		this.configurationClassesBasePackage = configurationsBasePackage;
		this.configurationClasses = configurationClasses;
	}

	public AdministrationConfigBeanRegistryPostProcessor( final String configurationsBasePackage ) {
		this( configurationsBasePackage, Collections.<Class>emptySet() );
	}

	public AdministrationConfigBeanRegistryPostProcessor( final Class... configurationClasses ) {
		this( null, Sets.newHashSet( configurationClasses ) );
	}

	@Override
	public void postProcessBeanFactory( final ConfigurableListableBeanFactory beanFactory ) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry( final BeanDefinitionRegistry registry ) throws BeansException {
		if ( configurationClassProvider == null ) {
			configurationClassProvider = new ConfigurationClassProvider();
		}

		if ( domainTypeEntityMetadataResolver == null ) {
			domainTypeEntityMetadataResolver = domainTypeEntityMetadataResolver( entityManager( registry ) );
		}

		if ( configurationParser == null ) {
			configurationParser = new DomainConfigurationParser( domainTypeEntityMetadataResolver );
		}

		if ( StringUtils.isNotBlank( configurationClassesBasePackage ) ) {
			configurationParser.parse( configurationClassProvider.findClassCandidates( configurationClassesBasePackage ) );
		}

		if ( !CollectionUtils.isEmpty( configurationClasses ) ) {
			configurationParser.parse( configurationClassProvider.findClassCandidates( configurationClasses ) );
		}

		configurationParser.validate();

		final Set<DomainConfigurationInterface> domainConfigurations = configurationParser.getDomainConfigurations();

		if ( beanDefinitionRegistrar == null ) {
			beanDefinitionRegistrar = compositeBeanDefinitionRegistrar( domainConfigurations );
		}

		beanDefinitionRegistrar.registerBeanDefinitions( registry );
	}

	private BeanDefinitionRegistrar compositeBeanDefinitionRegistrar( Set<DomainConfigurationInterface> domainConfigurations ) {
		BeanDefinitionRegistrar domainTypeRepositoryBeanDefinitionsRegistrar = new DomainTypeRepositoryBeanDefinitionRegistrar( domainConfigurations );
		BeanDefinitionRegistrar configurationBeanDefinitionRegistrar = new ConfigurationBeanDefinitionRegistrar( domainConfigurations );

		return new CompositeBeanDefinitionRegistrar( domainTypeRepositoryBeanDefinitionsRegistrar, configurationBeanDefinitionRegistrar );
	}

	private DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> domainTypeEntityMetadataResolver( EntityManager entityManager ) {
		return new JpaDomainTypeEntityMetadataResolver( entityManager );
	}

	private EntityManager entityManager( final BeanDefinitionRegistry registry ) {
		return ( ( BeanFactory ) registry ).getBean( EntityManagerFactory.class ).createEntityManager();
	}

	public void setBeanDefinitionRegistrar( final BeanDefinitionRegistrar beanDefinitionRegistrar ) {
		Assert.notNull( beanDefinitionRegistrar );

		this.beanDefinitionRegistrar = beanDefinitionRegistrar;
	}

	public void setDomainTypeEntityMetadataResolver( final DomainTypeEntityMetadataResolver<? extends DomainTypeEntityMetadata> domainTypeEntityMetadataResolver ) {
		Assert.notNull( domainTypeEntityMetadataResolver );

		this.domainTypeEntityMetadataResolver = domainTypeEntityMetadataResolver;
	}
}