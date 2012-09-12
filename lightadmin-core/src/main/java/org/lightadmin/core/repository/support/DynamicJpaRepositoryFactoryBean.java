package org.lightadmin.core.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class DynamicJpaRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

	private final Class<T> domainType;

	private EntityManager entityManager;

	public DynamicJpaRepositoryFactoryBean( final Class<T> domainType ) {
		this.domainType = domainType;
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory( EntityManager entityManager ) {
		return new DynamicRepositoryFactory( getEntityInformation(), entityManager );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public EntityInformation<T, I> getEntityInformation() {
		return ( EntityInformation<T, I> ) JpaEntityInformationSupport.getMetadata( domainType, entityManager );
	}

	@Override
	public RepositoryInformation getRepositoryInformation() {
		RepositoryMetadata metadata = new DynamicRepositoryMetadata( getObjectType(), getEntityInformation() );
		return new DynamicRepositoryInformation( metadata );
	}

	@PersistenceContext
	public void setEntityManager( EntityManager entityManager ) {
		super.setEntityManager( entityManager );
		this.entityManager = entityManager;
	}
}