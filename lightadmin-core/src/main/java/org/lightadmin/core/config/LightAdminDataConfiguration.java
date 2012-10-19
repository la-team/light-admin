package org.lightadmin.core.config;

import org.lightadmin.core.persistence.metamodel.JpaDomainTypeEntityMetadataResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LightAdminDataConfiguration {

	@Bean
	public JpaDomainTypeEntityMetadataResolver jpaDomainTypeEntityMetadataResolver() {
		return new JpaDomainTypeEntityMetadataResolver();
	}
}