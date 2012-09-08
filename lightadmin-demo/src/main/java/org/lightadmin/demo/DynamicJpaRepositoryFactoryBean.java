package org.lightadmin.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class DynamicJpaRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

	private EntityManager entityManager;

	private Class<T> domainType;

	protected RepositoryFactorySupport createRepositoryFactory( EntityManager entityManager ) {
		return new DynamicRepositoryFactory<T, I>( domainType, getEntityInformation().getIdType(), entityManager );
	}

	@Override
	public EntityInformation<T, I> getEntityInformation() {
		return ( JpaEntityInformation<T, I> ) JpaEntityInformationSupport.getMetadata( domainType, entityManager );
	}

	@Override
	public RepositoryInformation getRepositoryInformation() {
		RepositoryMetadata metadata = new DynamicRepositoryMetadata<T, I>( domainType, getEntityInformation().getIdType(), DynamicJpaRepository.class );
		return new DynamicRepositoryInformation( metadata, DynamicJpaRepository.class, null );
	}

	@PersistenceContext
	public void setEntityManager( EntityManager entityManager ) {
		super.setEntityManager( entityManager );
		this.entityManager = entityManager;
	}

	public void setDomainType( final Class<T> domainType ) {
		this.domainType = domainType;
	}
}