package org.lightadmin.core.config;

import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.core.util.Pair;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class DomainTypeAdministrationConfiguration {

	private JpaEntityInformation<?, ? extends Serializable> entityInformation;

	private final Class<?> domainType;

	private final DynamicJpaRepository<?, ?> repository;

	private Set<Pair<String, String>> listColumns = newLinkedHashSet();

	public DomainTypeAdministrationConfiguration( final Class<?> domainType, final DynamicJpaRepository<?, ?> repository ) {
		this.domainType = domainType;
		this.repository = repository;
	}

	@PersistenceContext
	public void setEntityManager( EntityManager entityManager ) {
		entityInformation = JpaEntityInformationSupport.getMetadata( domainType, entityManager );
	}

	public JpaEntityInformation getEntityInformation() {
		return entityInformation;
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	public DynamicJpaRepository<?, ?> getRepository() {
		return repository;
	}

	public String getDomainTypeName() {
		return entityInformation.getEntityName();
	}

	public void setListColumns( final Set<Pair<String, String>> listColumns ) {
		this.listColumns = listColumns;
	}

	public Set<Pair<String, String>> getListColumns() {
		return listColumns;
	}
}