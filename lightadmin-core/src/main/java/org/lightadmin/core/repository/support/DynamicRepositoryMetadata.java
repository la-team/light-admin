package org.lightadmin.core.repository.support;

import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;

import java.io.Serializable;

public class DynamicRepositoryMetadata<T, I extends Serializable> extends AbstractRepositoryMetadata {

	private final Class<?> repositoryInterface;
	private final Class<I> idType;
	private final Class<T> domainType;

	public DynamicRepositoryMetadata( final Class<T> domainType, final Class<I> idType, Class<?> repositoryInterface ) {
		super( repositoryInterface );
		this.domainType = domainType;
		this.idType = idType;
		this.repositoryInterface = repositoryInterface;
	}

	public Class<? extends Serializable> getIdType() {
		return idType;
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	public Class<?> getRepositoryInterface() {
		return repositoryInterface;
	}
}