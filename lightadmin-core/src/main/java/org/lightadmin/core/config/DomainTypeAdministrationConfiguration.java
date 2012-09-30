package org.lightadmin.core.config;

import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.core.repository.support.DomainEntityMetadata;
import org.lightadmin.core.view.ScreenContext;
import org.lightadmin.core.view.support.Filters;
import org.lightadmin.core.view.support.Fragment;
import org.lightadmin.core.view.support.Scopes;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.rest.repository.EntityMetadata;
import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class DomainTypeAdministrationConfiguration {

	private JpaEntityInformation<?, ? extends Serializable> entityInformation;

	private EntityMetadata<JpaAttributeMetadata> entityMetadata;

	private final Class<?> domainType;

	private final DynamicJpaRepository<?, ?> repository;

	private Fragment listViewFragment;

	private ScreenContext screenContext;

	private Scopes scopes;

	private Filters filters;

	public DomainTypeAdministrationConfiguration( final Class<?> domainType, final DynamicJpaRepository<?, ?> repository ) {
		Assert.notNull( domainType );
		Assert.notNull( repository );

		this.domainType = domainType;
		this.repository = repository;
	}

	@PersistenceContext
	public void setEntityManager( EntityManager entityManager ) {
		entityInformation = JpaEntityInformationSupport.getMetadata( domainType, entityManager );

		entityMetadata = new DomainEntityMetadata( entityManager.getMetamodel().entity( entityInformation.getJavaType() ));
	}

	public JpaEntityInformation getEntityInformation() {
		return entityInformation;
	}

	public EntityMetadata<JpaAttributeMetadata> getEntityMetadata() {
		return entityMetadata;
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	public DynamicJpaRepository<?, ?> getRepository() {
		return repository;
	}

	public String getDomainTypeName() {
		return StringUtils.uncapitalize( entityInformation.getEntityName() );
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
}