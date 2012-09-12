package org.lightadmin.core.repository.support;

import org.lightadmin.core.repository.DynamicJpaRepository;
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

// TODO: max: Kill this later!
public class DynamicRepositoriesDecorator extends Repositories {

	private final Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>> domainClassToBeanName = new HashMap<Class<?>, RepositoryFactoryInformation<Object, Serializable>>();
	private final Map<RepositoryFactoryInformation<Object, Serializable>, CrudRepository<Object, Serializable>> repositories = new HashMap<RepositoryFactoryInformation<Object, Serializable>, CrudRepository<Object, Serializable>>();

	/**
	 * Creates a new {@link Repositories} instance by looking up the repository instances and meta information from the
	 * given {@link ListableBeanFactory}.
	 *
	 * @param factory must not be {@literal null}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DynamicRepositoriesDecorator(ListableBeanFactory factory) {
		super( new StaticListableBeanFactory() );

		Collection<RepositoryFactoryInformation> providers = BeanFactoryUtils.beansOfTypeIncludingAncestors( factory, RepositoryFactoryInformation.class ).values();

		for (RepositoryFactoryInformation<Object, Serializable> info : providers) {

			RepositoryInformation information = info.getRepositoryInformation();
			Class repositoryInterface = information.getRepositoryInterface();

			if (DynamicJpaRepository.class.isAssignableFrom(repositoryInterface)) {
				Class<DynamicJpaRepository<Object, Serializable>> objectType = repositoryInterface;
				// Just one Repository implementation bean expected per domain type
				final Map<String, DynamicJpaRepository<Object, Serializable>> jpaRepositories = BeanFactoryUtils.beansOfTypeIncludingAncestors( factory, objectType );

				this.domainClassToBeanName.put(information.getDomainType(), info);
				this.repositories.put(info, jpaRepositories.get( repositoryBeanName( information.getDomainType() ) ));
			}
		}
	}

	private String repositoryBeanName( final Class<?> domainType ) {
		return StringUtils.uncapitalize( domainType.getSimpleName() ) + "Repository";
	}

	/**
	 * Returns whether we have a repository instance registered to manage instances of the given domain class.
	 *
	 * @param domainClass must not be {@literal null}.
	 * @return
	 */
	public boolean hasRepositoryFor(Class<?> domainClass) {
		return domainClassToBeanName.containsKey(domainClass);
	}

	/**
	 * Returns the repository managing the given domain class.
	 *
	 * @param domainClass must not be {@literal null}.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T, S extends Serializable> CrudRepository<T, S> getRepositoryFor(Class<?> domainClass) {
		return (CrudRepository<T, S>) repositories.get(domainClassToBeanName.get(domainClass));
	}

	/**
	 * Returns the {@link org.springframework.data.repository.core.EntityInformation} for the given domain class.
	 *
	 * @param domainClass must not be {@literal null}.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T, S extends Serializable> EntityInformation<T, S> getEntityInformationFor(Class<?> domainClass) {

		RepositoryFactoryInformation<Object, Serializable> information = getRepoInfoFor(domainClass);
		return information == null ? null : (EntityInformation<T, S>) information.getEntityInformation();
	}

	/**
	 * Returns the {@link EntityInformation} for the given domain class.
	 *
	 * @param domainClass must not be {@literal null}.
	 * @return the {@link EntityInformation} for the given domain class or {@literal null} if no repository registered for
	 *         this domain class.
	 */
	public RepositoryInformation getRepositoryInformationFor(Class<?> domainClass) {

		RepositoryFactoryInformation<Object, Serializable> information = getRepoInfoFor(domainClass);
		return information == null ? null : information.getRepositoryInformation();
	}

	/**
	 * Returns the {@link org.springframework.data.repository.query.QueryMethod}s contained in the repository managing the given domain class.
	 *
	 * @param domainClass must not be {@literal null}.
	 * @return
	 */
	public List<QueryMethod> getQueryMethodsFor(Class<?> domainClass) {

		RepositoryFactoryInformation<Object, Serializable> information = getRepoInfoFor(domainClass);
		return information == null ? Collections.<QueryMethod> emptyList() : information.getQueryMethods();
	}

	private RepositoryFactoryInformation<Object, Serializable> getRepoInfoFor(Class<?> domainClass) {

		Assert.notNull(domainClass);

		for (RepositoryFactoryInformation<Object, Serializable> information : repositories.keySet()) {
			if (domainClass.equals(information.getEntityInformation().getJavaType())) {
				return information;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Class<?>> iterator() {
		return domainClassToBeanName.keySet().iterator();
	}
}