package org.lightadmin.core.persistence.repository.support;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.lightadmin.core.persistence.repository.DynamicJpaRepositoryImpl;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.LockModeRepositoryPostProcessor;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@SuppressWarnings( {"rawtypes", "unchecked"} )
public class DynamicRepositoryFactory extends JpaRepositoryFactory {

	private final List<RepositoryProxyPostProcessor> repositoryPostProcessors;

	private final EntityInformation<?, ? extends Serializable> entityInformation;

	public DynamicRepositoryFactory( final EntityInformation<?, ? extends Serializable> entityInformation, final EntityManager entityManager ) {
		super( entityManager );

		this.entityInformation = entityInformation;

		this.repositoryPostProcessors = newArrayList();
		this.repositoryPostProcessors.add( LockModeRepositoryPostProcessor.INSTANCE );
	}

	@Override
	protected <T, ID extends Serializable> JpaRepository<?, ?> getTargetRepository( RepositoryMetadata metadata, EntityManager entityManager ) {
		return new DynamicJpaRepositoryImpl( metadata.getDomainType(), entityManager );
	}

	@Override
	protected Class<?> getRepositoryBaseClass( final RepositoryMetadata metadata ) {
		return DynamicJpaRepository.class;
	}

	@Override
	protected RepositoryInformation getRepositoryInformation( final RepositoryMetadata metadata, final Class<?> customImplementationClass ) {
		return new DynamicRepositoryInformation( metadata );
	}

	@Override
	public <T> T getRepository( final Class<T> repositoryInterface, final Object customImplementation ) {
		RepositoryMetadata metadata = getRepositoryMetadata( repositoryInterface );
		RepositoryInformation information = getRepositoryInformation( metadata, null );

		Object target = getTargetRepository( information );

		ProxyFactory result = new ProxyFactory();
		result.setTarget( target );
		result.setInterfaces( new Class[] {repositoryInterface, Repository.class} );

		for ( RepositoryProxyPostProcessor processor : repositoryPostProcessors ) {
			processor.postProcess( result );
		}

		result.addAdvice( new QueryExecutorMethodInterceptor( information, customImplementation, target ) );

		return ( T ) result.getProxy();
	}

	private RepositoryMetadata getRepositoryMetadata( Class<?> repositoryInterface ) {
		return new DynamicRepositoryMetadata( repositoryInterface, entityInformation );
	}
}