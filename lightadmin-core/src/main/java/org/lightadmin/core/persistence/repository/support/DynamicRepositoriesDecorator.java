package org.lightadmin.core.persistence.repository.support;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

import static com.google.common.collect.Maps.newHashMap;

public class DynamicRepositoriesDecorator extends Repositories {

	private final Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>> domainClassToBeanName = newHashMap();
	private final Map<RepositoryFactoryInformation<Object, Serializable>, DynamicJpaRepository<Object, Serializable>> repositories = newHashMap();

	@SuppressWarnings( {"rawtypes", "unchecked"} )
	public DynamicRepositoriesDecorator( ListableBeanFactory factory ) {
		super( emptyBeanFactory() );

		final Collection<DynamicJpaRepositoryFactoryBean> providers = BeanFactoryUtils.beansOfTypeIncludingAncestors( factory, DynamicJpaRepositoryFactoryBean.class ).values();

		final Map<String, DynamicJpaRepository> jpaRepositories = dynamicJpaRepositories( factory );

		for ( RepositoryFactoryInformation<Object, Serializable> info : providers ) {
			final Class<?> domainType = info.getRepositoryInformation().getDomainType();

			this.domainClassToBeanName.put( domainType, info );
			this.repositories.put( info, jpaRepositories.get( repositoryBeanName( domainType ) ) );
		}
	}

	@SuppressWarnings( {"rawtypes", "unchecked"} )
	private Map<String, DynamicJpaRepository> dynamicJpaRepositories( final ListableBeanFactory factory ) {
		return BeanFactoryUtils.beansOfTypeIncludingAncestors( factory, DynamicJpaRepository.class );
	}

	private static StaticListableBeanFactory emptyBeanFactory() {
		return new StaticListableBeanFactory();
	}

	private String repositoryBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "Repository";
	}

	/**
	 * Returns whether we have a repository instance registered to manage instances of the given domain class.
	 */
	public boolean hasRepositoryFor( Class<?> domainClass ) {
		return domainClassToBeanName.containsKey( domainClass );
	}

	/**
	 * Returns the repository managing the given domain class.
	 */
	@SuppressWarnings( "unchecked" )
	public <T, S extends Serializable> CrudRepository<T, S> getRepositoryFor( Class<?> domainClass ) {
		return ( CrudRepository<T, S> ) repositories.get( domainClassToBeanName.get( domainClass ) );
	}

	/**
	 * Returns the {@link org.springframework.data.repository.core.EntityInformation} for the given domain class.
	 */
	@SuppressWarnings( "unchecked" )
	public <T, S extends Serializable> EntityInformation<T, S> getEntityInformationFor( Class<?> domainClass ) {
		RepositoryFactoryInformation<Object, Serializable> information = getRepoInfoFor( domainClass );
		return information == null ? null : ( EntityInformation<T, S> ) information.getEntityInformation();
	}

	/**
	 * Returns the {@link EntityInformation} for the given domain class.
	 *
	 * @param domainClass must not be {@literal null}.
	 * @return the {@link EntityInformation} for the given domain class or {@literal null} if no repository registered for
	 *         this domain class.
	 */
	public RepositoryInformation getRepositoryInformationFor( Class<?> domainClass ) {
		RepositoryFactoryInformation<Object, Serializable> information = getRepoInfoFor( domainClass );
		return information == null ? null : information.getRepositoryInformation();
	}

	public List<QueryMethod> getQueryMethodsFor( Class<?> domainClass ) {
		return Collections.emptyList();
	}

	private RepositoryFactoryInformation<Object, Serializable> getRepoInfoFor( Class<?> domainClass ) {
		Assert.notNull( domainClass );
		for ( RepositoryFactoryInformation<Object, Serializable> information : repositories.keySet() ) {
			if ( domainClass.equals( information.getEntityInformation().getJavaType() ) ) {
				return information;
			}
		}
		return null;
	}

	public Iterator<Class<?>> iterator() {
		return domainClassToBeanName.keySet().iterator();
	}
}