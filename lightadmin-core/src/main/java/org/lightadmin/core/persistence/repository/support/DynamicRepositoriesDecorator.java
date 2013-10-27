package org.lightadmin.core.persistence.repository.support;

import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newLinkedHashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DynamicRepositoriesDecorator extends Repositories {

    private final Map<Class<?>, DomainTypeBasicConfiguration> domainTypeConfigurations;

    private final DomainTypeEntityMetadataResolver domainTypeEntityMetadataResolver;

    public DynamicRepositoriesDecorator(GlobalAdministrationConfiguration globalAdministrationConfiguration, DomainTypeEntityMetadataResolver domainTypeEntityMetadataResolver) {
        super(emptyBeanFactory());

        this.domainTypeEntityMetadataResolver = domainTypeEntityMetadataResolver;
        this.domainTypeConfigurations = newLinkedHashMap(globalAdministrationConfiguration.getDomainTypeConfigurations());

    }

    @Override
    public boolean hasRepositoryFor(Class<?> domainClass) {
        return domainTypeConfigurations.containsKey(domainClass);
    }

    @Override
    public <T, S extends Serializable> CrudRepository<T, S> getRepositoryFor(Class<?> domainClass) {
        if (hasRepositoryFor(domainClass)) {
            return (CrudRepository<T, S>) domainTypeConfigurations.get(domainClass).getRepository();
        }
        return null;
    }

    @Override
    public <T, S extends Serializable> EntityInformation<T, S> getEntityInformationFor(Class<?> domainClass) {
        RepositoryFactoryInformation<Object, Serializable> information = getRepoInfoFor(domainClass);
        return information == null ? null : (EntityInformation<T, S>) information.getEntityInformation();
    }

    @Override
    public RepositoryInformation getRepositoryInformationFor(Class<?> domainClass) {
        RepositoryFactoryInformation<Object, Serializable> information = getRepoInfoFor(domainClass);
        return information == null ? null : information.getRepositoryInformation();
    }

    @Override
    public List<QueryMethod> getQueryMethodsFor(Class<?> domainClass) {
        return Collections.emptyList();
    }

    @Override
    public Iterator<Class<?>> iterator() {
        return domainTypeConfigurations.keySet().iterator();
    }

    private static StaticListableBeanFactory emptyBeanFactory() {
        return new StaticListableBeanFactory();
    }

    private RepositoryFactoryInformation<Object, Serializable> getRepoInfoFor(Class<?> domainClass) {
        Assert.notNull(domainClass);
        if (hasRepositoryFor(domainClass)) {
            return new ConfigurationRepositoryFactoryInformationAdapter(domainTypeConfigurations.get(domainClass));

        }
        return null;
    }

    private class ConfigurationRepositoryFactoryInformationAdapter<T, ID extends Serializable> implements RepositoryFactoryInformation<T, ID> {

        private final DomainTypeBasicConfiguration domainTypeConfiguration;

        public ConfigurationRepositoryFactoryInformationAdapter(final DomainTypeBasicConfiguration domainTypeAdministrationConfiguration) {
            this.domainTypeConfiguration = domainTypeAdministrationConfiguration;
        }

        @Override
        @SuppressWarnings("unchecked")
        public EntityInformation<T, ID> getEntityInformation() {
            return domainTypeEntityMetadataResolver.getEntityInformation(domainTypeConfiguration.getDomainType());
        }

        @Override
        public RepositoryInformation getRepositoryInformation() {
            RepositoryMetadata metadata = new DynamicRepositoryMetadata(getEntityInformation());
            return new DynamicRepositoryInformation(metadata);
        }

        @Override
        public List<QueryMethod> getQueryMethods() {
            return Collections.emptyList();
        }
    }
}
