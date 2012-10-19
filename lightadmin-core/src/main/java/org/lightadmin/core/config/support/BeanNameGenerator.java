package org.lightadmin.core.config.support;

import org.springframework.util.StringUtils;

import static org.springframework.util.StringUtils.uncapitalize;

public final class BeanNameGenerator {

	private static final String REPOSITORY_BEAN_NAME_FORMAT = "org.lightadmin.core.persistence.repository.%sRepository";

	private static final String DOMAIN_CONFIGURATION_BEAN_NAME_FORMAT = "org.lightadmin.core.config.%sAdministrationConfiguration";

	private static final String GLOBAL_ADMINISTRATION_CONFIGURATION_BEAN_NAME = "org.lightadmin.core.config.globalAdministrationConfiguration";

	public static final BeanNameGenerator INSTANCE = new BeanNameGenerator();

	private BeanNameGenerator() {
	}

	public String repositoryBeanName( final Class<?> domainType ) {
		return String.format( REPOSITORY_BEAN_NAME_FORMAT, uncapitalize( domainType.getSimpleName() ) );
	}

	public String globalAdministrationConfiguration() {
		return GLOBAL_ADMINISTRATION_CONFIGURATION_BEAN_NAME;
	}

	public String domainTypeConfigurationBeanName( final Class<?> domainType ) {
		return String.format( DOMAIN_CONFIGURATION_BEAN_NAME_FORMAT, uncapitalize( domainType.getSimpleName() ) );
	}

	public String repositoryServiceExporterName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() );
	}
}