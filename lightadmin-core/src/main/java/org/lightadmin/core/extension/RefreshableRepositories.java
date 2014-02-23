package org.lightadmin.core.extension;

import org.lightadmin.core.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.support.Repositories;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RefreshableRepositories extends Repositories implements Refreshable, ApplicationListener<DynamicRepositoryBeanFactoryRefreshedEvent>, ApplicationEventPublisherAware {

    private static final String DOMAIN_CLASS_TO_BEAN_NAME_FIELD = "domainClassToBeanName";
    private static final String REPOSITORIES_FIELD = "repositories";
    private static final String REPOSITORY_FACTORY_BEAN_NAMES_FIELD = "repositoryFactoryBeanNames";

    private final ListableBeanFactory factory;

    private final BeanWrapper delegateBeanWrapper;
    private ApplicationEventPublisher applicationEventPublisher;

    public RefreshableRepositories(ListableBeanFactory factory) {
        super(factory);

        this.factory = factory;
        this.delegateBeanWrapper = new DirectFieldAccessFallbackBeanWrapper(this);
    }

    @Override
    public void refresh() {
        emptyDomainClassToBeanNamesCache();
        emptyRepositoriesCache();
        emptyRepositoryFactoryBeanNames();

        repopulateCache();

        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(new RepositoriesRefreshedEvent(this));
        }
    }

    @Override
    public void onApplicationEvent(DynamicRepositoryBeanFactoryRefreshedEvent event) {
        refresh();
    }

    private void repopulateCache() {
        repositoryFactoryBeanNames().addAll(Arrays.asList(factory.getBeanDefinitionNames()));
    }

    @SuppressWarnings("unchecked")
    private Set<String> repositoryFactoryBeanNames() {
        return (Set<String>) delegateBeanWrapper.getPropertyValue(REPOSITORY_FACTORY_BEAN_NAMES_FIELD);
    }

    private void emptyDomainClassToBeanNamesCache() {
        this.delegateBeanWrapper.setPropertyValue(DOMAIN_CLASS_TO_BEAN_NAME_FIELD, new HashMap<Class<?>, RepositoryFactoryInformation<Object, Serializable>>());
    }

    private void emptyRepositoriesCache() {
        this.delegateBeanWrapper.setPropertyValue(REPOSITORIES_FIELD, new HashMap<RepositoryFactoryInformation<Object, Serializable>, String>());
    }

    private void emptyRepositoryFactoryBeanNames() {
        this.delegateBeanWrapper.setPropertyValue(REPOSITORY_FACTORY_BEAN_NAMES_FIELD, new HashSet<String>());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}