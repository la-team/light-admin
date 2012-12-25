package org.lightadmin.core.config.domain;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSource;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.show.ShowViewConfigurationUnit;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class DomainTypeAdministrationConfiguration implements DomainTypeBasicConfiguration {

	private final DynamicJpaRepository<?, ? extends Serializable> repository;

	private final DomainConfigurationSource domainConfigurationSource;

	public DomainTypeAdministrationConfiguration( DomainConfigurationSource domainConfigurationSource, final DynamicJpaRepository<?, ?> repository ) {
		Assert.notNull( domainConfigurationSource );
		Assert.notNull( repository );

		this.domainConfigurationSource = domainConfigurationSource;
		this.repository = repository;
	}

	@Override
	public DomainTypeEntityMetadata getDomainTypeEntityMetadata() {
		return domainConfigurationSource.getDomainTypeEntityMetadata();
	}

	@Override
	public Class<?> getDomainType() {
		return domainConfigurationSource.getDomainType();
	}

	@Override
	public DynamicJpaRepository<?, ?> getRepository() {
		return repository;
	}

	@Override
	public String getDomainTypeName() {
		return StringUtils.uncapitalize( getDomainTypeEntityMetadata().getEntityName() );
	}

	public Fragment getListViewFragment() {
		return domainConfigurationSource.getListViewFragment().getFragment();
	}

	public ShowViewConfigurationUnit getShowViewFragment() {
		return domainConfigurationSource.getShowViewFragment();
	}

	public FieldSetConfigurationUnit getFormViewFragment() {
		return domainConfigurationSource.getFormViewFragment();
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

	@Override
	public EntityMetadataConfigurationUnit getEntityConfiguration() {
		return domainConfigurationSource.getEntityConfiguration();
	}

	@Override
	public String getConfigurationName() {
		return domainConfigurationSource.getConfigurationName();
	}
}
