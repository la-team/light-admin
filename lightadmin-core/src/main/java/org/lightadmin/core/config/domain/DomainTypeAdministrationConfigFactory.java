package org.lightadmin.core.config.domain;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.DynamicJpaRepositoryFactory;

import java.io.Serializable;

public class DomainTypeAdministrationConfigFactory {

	private final DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory;

	public DomainTypeAdministrationConfigFactory( final DynamicJpaRepositoryFactory dynamicJpaRepositoryFactory ) {
		this.dynamicJpaRepositoryFactory = dynamicJpaRepositoryFactory;
	}

	@SuppressWarnings( "unchecked" )
	public DomainTypeAdministrationConfiguration createAdministrationConfiguration( DomainConfigurationSource domainConfigurationSource ) {
		final DynamicJpaRepository<?, ? extends Serializable> repository = dynamicJpaRepositoryFactory.createRepository( domainConfigurationSource.getDomainType() );

		DomainTypeAdministrationConfiguration domainTypeAdministrationConfiguration = new DomainTypeAdministrationConfiguration( domainConfigurationSource.getDomainType(), repository );

		domainTypeAdministrationConfiguration.setConfigurationName( domainConfigurationSource.getConfigurationName() );
		domainTypeAdministrationConfiguration.setDomainTypeEntityMetadata( domainConfigurationSource.getDomainTypeEntityMetadata() );
		domainTypeAdministrationConfiguration.setEntityConfiguration( domainConfigurationSource.getEntityConfiguration() );
		domainTypeAdministrationConfiguration.setScreenContext( domainConfigurationSource.getScreenContext() );
		domainTypeAdministrationConfiguration.setListViewFragment( domainConfigurationSource.getListViewFragment() );
		domainTypeAdministrationConfiguration.setScopes( domainConfigurationSource.getScopes() );
		domainTypeAdministrationConfiguration.setFilters( domainConfigurationSource.getFilters() );

		return domainTypeAdministrationConfiguration;
	}
}