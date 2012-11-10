package org.lightadmin.core.config;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.rest.DomainTypeToResourceConverter;
import org.lightadmin.core.rest.DynamicJpaRepositoryExporter;
import org.lightadmin.core.rest.DynamicRepositoryRestController;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	public DynamicJpaRepositoryExporter jpaRepositoryExporter() {
		return new DynamicJpaRepositoryExporter();
	}

	@Override
	public DynamicRepositoryRestController repositoryRestController() throws Exception {
		return new DynamicRepositoryRestController();
	}

	@Bean
	@Autowired
	public DomainTypeToResourceConverter domainTypeToResourceConverter( GlobalAdministrationConfiguration configuration ) {
		return new DomainTypeToResourceConverter( repositoryRestConfiguration(), jpaRepositoryExporter(), configuration );
	}
}