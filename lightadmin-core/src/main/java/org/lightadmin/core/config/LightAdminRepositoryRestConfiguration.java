package org.lightadmin.core.config;

import org.lightadmin.core.rest.DomainTypeToResourceConverter;
import org.lightadmin.core.rest.DynamicJpaRepositoryExporter;
import org.lightadmin.core.rest.DynamicRepositoryRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.RepositoryRestMvcConfiguration;

@Configuration
public class LightAdminRepositoryRestConfiguration extends RepositoryRestMvcConfiguration {

	@Bean
	public RepositoryRestConfiguration repositoryRestConfiguration() {
		return RepositoryRestConfiguration.DEFAULT.setDefaultPageSize( 10 );
	}

	@Bean
	@Override
	public DynamicJpaRepositoryExporter jpaRepositoryExporter() {
		return new DynamicJpaRepositoryExporter();
	}

	@Bean
	@Override
	public DynamicRepositoryRestController repositoryRestController() throws Exception {
		return new DynamicRepositoryRestController();
	}

	@Bean
	public DomainTypeToResourceConverter domainTypeToResourceConverter() {
		return new DomainTypeToResourceConverter();
	}
}