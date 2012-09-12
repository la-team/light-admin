package org.lightadmin.core.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;

public class DynamicRepositoryMetadata implements RepositoryMetadata {

	private final Class<?> repositoryInterface;

	private EntityInformation<?, ? extends Serializable> entityInformation;

	public DynamicRepositoryMetadata( Class<?> repositoryInterface, EntityInformation<?, ? extends Serializable> entityInformation ) {
		Assert.isTrue( repositoryInterface.isInterface() );
		Assert.isTrue( JpaRepository.class.isAssignableFrom( repositoryInterface ) );

		Assert.notNull( entityInformation );

		this.repositoryInterface = repositoryInterface;
		this.entityInformation = entityInformation;
	}

	@Override
	public Class<? extends Serializable> getIdType() {
		return entityInformation.getIdType();
	}

	@Override
	public Class<?> getDomainType() {
		return entityInformation.getJavaType();
	}

	@Override
	public Class<?> getRepositoryInterface() {
		return repositoryInterface;
	}

	@Override
	public Class<?> getReturnedDomainClass( final Method method ) {
		return entityInformation.getJavaType();
	}
}