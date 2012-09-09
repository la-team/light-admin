package org.lightadmin.demo;

public class DomainTypeAdministrationConfiguration {

	private final Class<?> domainType;

	private final DynamicJpaRepository<?, ?> repository;

	private String domainTypeName;

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
}