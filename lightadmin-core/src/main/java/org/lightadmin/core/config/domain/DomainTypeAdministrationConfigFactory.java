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

		return new DomainTypeAdministrationConfiguration( domainConfigurationSource, repository );
	}
}