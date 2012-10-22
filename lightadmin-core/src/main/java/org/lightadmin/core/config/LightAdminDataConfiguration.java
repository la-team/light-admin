package org.lightadmin.core.config;

import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadataResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class LightAdminDataConfiguration {

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JpaDomainTypeEntityMetadataResolver jpaDomainTypeEntityMetadataResolver() {
		return new JpaDomainTypeEntityMetadataResolver( entityManager );
	}
}