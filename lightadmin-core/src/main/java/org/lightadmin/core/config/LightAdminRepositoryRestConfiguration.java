package org.lightadmin.core.config;

import org.lightadmin.core.rest.DomainTypeToResourceConverter;
import org.lightadmin.core.rest.DynamicJpaRepositoryExporter;
import org.lightadmin.core.rest.DynamicRepositoryRestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.RepositoryRestMvcConfiguration;

@Lazy
@Configuration
public class LightAdminRepositoryRestConfiguration extends RepositoryRestMvcConfiguration {

	@Bean
	public RepositoryRestConfiguration repositoryRestConfiguration() {
		return RepositoryRestConfiguration.DEFAULT.setDefaultPageSize( 10 );
	}

	@Override
	@Bean
	public DynamicJpaRepositoryExporter jpaRepositoryExporter() {
		return new DynamicJpaRepositoryExporter();
	}

	@Override
	@Bean
	public DynamicRepositoryRestController repositoryRestController() throws Exception {
		return new DynamicRepositoryRestController();
	}

	@Bean
	public DomainTypeToResourceConverter domainTypeToResourceConverter() {
		return new DomainTypeToResourceConverter();
	}
}