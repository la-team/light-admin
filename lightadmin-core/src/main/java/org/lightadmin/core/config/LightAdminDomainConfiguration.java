package org.lightadmin.core.config;

import org.lightadmin.core.config.bootstrap.GlobalAdministrationConfigurationProcessor;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationClassSourceValidator;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidator;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.repository.DynamicJpaRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class LightAdminDomainConfiguration {

	@Autowired
	private Environment environment;

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public DomainTypeEntityMetadataResolver domainTypeEntityMetadataResolver() {
		return new JpaDomainTypeEntityMetadataResolver( entityManager );
	}

	@Bean
	@Autowired
	public DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory( TransactionInterceptor transactionInterceptor ) {
		return new DynamicJpaRepositoryFactory( entityManager, transactionInterceptor );
	}

	@Bean
	public DomainConfigurationSourceFactory domainConfigurationSourceFactory() {
		return new DomainConfigurationSourceFactory( domainTypeEntityMetadataResolver() );
	}

	@Bean
	@Autowired
	public DomainTypeAdministrationConfigFactory domainTypeAdministrationConfigFactory( DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory ) {
		return new DomainTypeAdministrationConfigFactory( dynamicJpaRepositoryFactory );
	}

	@Bean
	public GlobalAdministrationConfiguration globalAdministrationConfiguration() {
		return new GlobalAdministrationConfiguration();
	}

	@Bean
	@Autowired
	public GlobalAdministrationConfigurationProcessor globalAdministrationConfigurationProcessor( DomainTypeAdministrationConfigFactory domainTypeAdministrationConfigFactory ) {
		final DomainConfigurationSourceValidator configurationSourceValidator = new DomainConfigurationClassSourceValidator( domainTypeEntityMetadataResolver() );

		return new GlobalAdministrationConfigurationProcessor( domainTypeAdministrationConfigFactory,
															   domainConfigurationSourceFactory(),
															   configurationSourceValidator,
															   environment );
	}
}