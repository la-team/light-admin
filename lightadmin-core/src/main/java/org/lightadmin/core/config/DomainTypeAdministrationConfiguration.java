package org.lightadmin.core.config;

import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.Scopes;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class DomainTypeAdministrationConfiguration {

	private DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> domainTypeEntityMetadata;

	private final Class<?> domainType;

	private final DynamicJpaRepository<?, ? extends Serializable> repository;

	private EntityConfiguration entityConfiguration;

	private ScreenContext screenContext;

	private Fragment listViewFragment;

	private Scopes scopes;

	private Filters filters;

	public DomainTypeAdministrationConfiguration( final Class<?> domainType, final DynamicJpaRepository<?, ?> repository ) {
		Assert.notNull( domainType );
		Assert.notNull( repository );

		this.domainType = domainType;
		this.repository = repository;
	}

	public void setDomainTypeEntityMetadata( final DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> domainTypeEntityMetadata ) {
		this.domainTypeEntityMetadata = domainTypeEntityMetadata;
	}

	public DomainTypeEntityMetadata<? extends DomainTypeAttributeMetadata> getDomainTypeEntityMetadata() {
		return domainTypeEntityMetadata;
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	public DynamicJpaRepository<?, ?> getRepository() {
		return repository;
	}

	public String getDomainTypeName() {
		return StringUtils.uncapitalize( domainTypeEntityMetadata.getEntityName() );
	}

	public Fragment getListViewFragment() {
		return listViewFragment;
	}

	public void setListViewFragment( final Fragment listViewFragment ) {
		this.listViewFragment = listViewFragment;
	}

	public void setScreenContext( final ScreenContext screenContext ) {
		this.screenContext = screenContext;
	}

	public ScreenContext getScreenContext() {
		return this.screenContext;
	}

	public Scopes getScopes() {
		return scopes;
	}

	public void setScopes( final Scopes scopes ) {
		this.scopes = scopes;
	}

	public Filters getFilters() {
		return filters;
	}

	public void setFilters( final Filters filters ) {
		this.filters = filters;
	}

	public EntityConfiguration getEntityConfiguration() {
		return entityConfiguration;
	}

	public void setEntityConfiguration( final EntityConfiguration entityConfiguration ) {
		this.entityConfiguration = entityConfiguration;
	}
}