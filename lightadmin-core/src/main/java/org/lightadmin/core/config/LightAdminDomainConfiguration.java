package org.lightadmin.core.config;

import org.lightadmin.core.config.bootstrap.DomainTypeAdministrationConfigurationReader;
import org.lightadmin.core.config.bootstrap.GlobalAdministrationConfigurationProcessor;
import org.lightadmin.core.config.bootstrap.SimpleDomainTypeAdministrationConfigurationReader;
import org.lightadmin.core.config.bootstrap.parsing.DomainConfigurationClassSourceParser;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadataResolver;
import org.lightadmin.core.persistence.repository.DynamicJpaRepositoryFactory;
import org.lightadmin.core.reporting.ProblemReporterFactory;
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
	public JpaDomainTypeEntityMetadataResolver jpaDomainTypeEntityMetadataResolver() {
		return new JpaDomainTypeEntityMetadataResolver( entityManager );
	}

	@Bean
	@Autowired
	public DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory( TransactionInterceptor transactionInterceptor ) {
		return new DynamicJpaRepositoryFactory( entityManager, transactionInterceptor );
	}

	@Bean
	public GlobalAdministrationConfiguration globalAdministrationConfiguration() {
		return new GlobalAdministrationConfiguration();
	}

	@Bean
	@Autowired
	public DomainTypeAdministrationConfigurationReader<Class> domainTypeAdministrationConfigurationReader( DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory ) {
		return new SimpleDomainTypeAdministrationConfigurationReader(
			new DomainConfigurationClassSourceParser( jpaDomainTypeEntityMetadataResolver() ),
			dynamicJpaRepositoryFactory,
			ProblemReporterFactory.failFastReporter()
		);
	}

	@Bean
	@Autowired
	public GlobalAdministrationConfigurationProcessor globalAdministrationConfigurationProcessor( DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory ) {
		return new GlobalAdministrationConfigurationProcessor(
			domainTypeAdministrationConfigurationReader( dynamicJpaRepositoryFactory ),
			environment
		);
	}
}