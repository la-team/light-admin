package org.lightadmin.core.config;

import org.lightadmin.core.repository.DynamicJpaRepository;
import org.lightadmin.core.util.Pair;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class DomainTypeAdministrationConfiguration {

	private final Class<?> domainType;

	private final DynamicJpaRepository<?, ?> repository;

	private String domainTypeName;

	private Set<Pair<String, String>> listColumns = newLinkedHashSet();

	public DomainTypeAdministrationConfiguration( final Class<?> domainType, final DynamicJpaRepository<?, ?> repository ) {
		this.domainType = domainType;
		this.repository = repository;
		this.domainTypeName = domainType.getSimpleName();
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	public DynamicJpaRepository<?, ?> getRepository() {
		return repository;
	}

	public String getDomainTypeName() {
		return domainTypeName;
	}

	public void setListColumns( final Set<Pair<String, String>> listColumns ) {
		this.listColumns = listColumns;
	}

	public Set<Pair<String, String>> getListColumns() {
		return listColumns;
	}
}