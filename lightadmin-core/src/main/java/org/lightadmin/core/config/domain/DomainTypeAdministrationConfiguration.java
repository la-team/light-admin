package org.lightadmin.core.config.domain;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class DomainTypeAdministrationConfiguration {

	private final DynamicJpaRepository<?, ? extends Serializable> repository;

	private final DomainConfigurationSource domainConfigurationSource;

	public DomainTypeAdministrationConfiguration( DomainConfigurationSource domainConfigurationSource, final DynamicJpaRepository<?, ?> repository ) {
		Assert.notNull( domainConfigurationSource );
		Assert.notNull( repository );

		this.domainConfigurationSource = domainConfigurationSource;
		this.repository = repository;
	}

	public DomainTypeEntityMetadata getDomainTypeEntityMetadata() {
		return domainConfigurationSource.getDomainTypeEntityMetadata();
	}

	public Class<?> getDomainType() {
		return domainConfigurationSource.getDomainType();
	}

	public DynamicJpaRepository<?, ?> getRepository() {
		return repository;
	}

	public String getDomainTypeName() {
		return StringUtils.uncapitalize( getDomainTypeEntityMetadata().getEntityName() );
	}

	public Fragment getListViewFragment() {
		return domainConfigurationSource.getListViewFragment().getFragment();
	}

	public ShowViewConfigurationUnit getShowViewFragment() {
		return domainConfigurationSource.getShowViewFragment();
	}

	public ScreenContextConfigurationUnit getScreenContext() {
		return domainConfigurationSource.getScreenContext();
	}

	public ScopesConfigurationUnit getScopes() {
		return domainConfigurationSource.getScopes();
	}

	public FiltersConfigurationUnit getFilters() {
		return domainConfigurationSource.getFilters();
	}

	public EntityMetadataConfigurationUnit getEntityConfiguration() {
		return domainConfigurationSource.getEntityConfiguration();
	}

	public String getConfigurationName() {
		return domainConfigurationSource.getConfigurationName();
	}
}