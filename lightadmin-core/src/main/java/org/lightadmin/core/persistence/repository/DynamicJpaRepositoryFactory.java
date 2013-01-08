package org.lightadmin.core.persistence.repository;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class DynamicJpaRepositoryFactory {

	private final EntityManager entityManager;

	private final TransactionInterceptor transactionInterceptor;

	public DynamicJpaRepositoryFactory( EntityManager entityManager, TransactionInterceptor transactionInterceptor ) {
		Assert.notNull( entityManager );

		this.entityManager = entityManager;
		this.transactionInterceptor = transactionInterceptor;
	}

	public <T> DynamicJpaRepository<T, Serializable> createRepository( Class<T> domainType ) {
		final DynamicJpaRepository<T, Serializable> dynamicJpaRepository = createDynamicRepository( domainType );

		if ( transactionInterceptor == null ) {
			return dynamicJpaRepository;
		}

		return decorateTransactional( dynamicJpaRepository, transactionInterceptor );
	}

	@SuppressWarnings("unchecked")
	private <T> DynamicJpaRepository<T, Serializable> decorateTransactional( DynamicJpaRepository<T, Serializable> dynamicJpaRepository, final TransactionInterceptor transactionInterceptor ) {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setTarget( dynamicJpaRepository );
		proxyFactory.setInterfaces( new Class[] {DynamicJpaRepository.class} );

		proxyFactory.addAdvice( transactionInterceptor );

		return ( DynamicJpaRepository<T, Serializable> ) proxyFactory.getProxy();
	}

	private <T> DynamicJpaRepository<T, Serializable> createDynamicRepository( final Class<T> domainType ) {
		return new DynamicJpaRepositoryImpl<T, Serializable>( domainType, entityManager );
	}
}