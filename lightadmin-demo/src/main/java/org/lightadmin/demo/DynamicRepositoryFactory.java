package org.lightadmin.demo;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.LockModeRepositoryPostProcessor;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class DynamicRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

	private final EntityManager entityManager;

	private final List<RepositoryProxyPostProcessor> repositoryPostProcessors;

	private final Class<T> domainType;
	private final Class<I> idType;

	public DynamicRepositoryFactory( final Class<T> domainType, final Class<I> idType, final EntityManager entityManager ) {
		super( entityManager );

		this.entityManager = entityManager;
		this.domainType = domainType;
		this.idType = idType;

		this.repositoryPostProcessors = newArrayList();
		this.repositoryPostProcessors.add( LockModeRepositoryPostProcessor.INSTANCE );
	}

	@Override
	protected Object getTargetRepository( final RepositoryMetadata metadata ) {
		return new DynamicJpaRepositoryImpl<T, I>( domainType, entityManager );
	}

	@Override
	protected Class<?> getRepositoryBaseClass( final RepositoryMetadata metadata ) {
		return DynamicJpaRepository.class;
	}

	private RepositoryMetadata getRepositoryMetadata( Class<?> repositoryInterface ) {
		return new DynamicRepositoryMetadata<T, I>( domainType, idType, repositoryInterface );
	}

	@Override
	public <T, ID extends Serializable> JpaEntityInformation<T, ID> getEntityInformation( final Class<T> domainClass ) {
		return ( JpaEntityInformation<T, ID> ) JpaEntityInformationSupport.getMetadata( domainClass, entityManager );
	}

	@Override
	protected RepositoryInformation getRepositoryInformation( final org.springframework.data.repository.core.RepositoryMetadata metadata, final Class<?> customImplementationClass ) {
		return new DynamicRepositoryInformation( metadata, getRepositoryBaseClass( metadata ), customImplementationClass );
	}

	@Override
	public <T> T getRepository( final Class<T> repositoryInterface, final Object customImplementation ) {
		RepositoryMetadata metadata = getRepositoryMetadata( repositoryInterface );
		Class<?> customImplementationClass = null == customImplementation ? null : customImplementation.getClass();
		RepositoryInformation information = getRepositoryInformation( metadata, customImplementationClass );

		Object target = getTargetRepository( information );

		// Create proxy
		ProxyFactory result = new ProxyFactory();
		result.setTarget( target );
		result.setInterfaces( new Class[] {repositoryInterface, Repository.class} );

		for ( RepositoryProxyPostProcessor processor : repositoryPostProcessors ) {
			processor.postProcess( result );
		}

		result.addAdvice( new QueryExecutorMethodInterceptor( information, customImplementation, target ) );

		return ( T ) result.getProxy();
	}
}