package org.lightadmin.core.extension;

import org.lightadmin.core.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RefreshableRepositories extends Repositories implements Refreshable, ApplicationListener<DynamicRepositoryBeanFactoryRefreshedEvent>, ApplicationEventPublisherAware {

    private static final String REPOSITORY_BEAN_NAMES_FIELD = "repositoryBeanNames";
    private static final String REPOSITORY_FACTORY_INFOS_FIELD = "repositoryFactoryInfos";

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
        emptyRepositoryBeanNames();
        emptyRepositoryFactoryInfosCache();

        repopulateRepositoryFactoryInformation();

        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(new RepositoriesRefreshedEvent(this));
        }
    }

    @Override
    public void onApplicationEvent(DynamicRepositoryBeanFactoryRefreshedEvent event) {
        refresh();
    }

    @SuppressWarnings("unchecked")
    private void repopulateRepositoryFactoryInformation() {
        String[] repositoryBeanNames = factory.getBeanDefinitionNames();
        for (String repositoryBeanName : repositoryBeanNames) {
            RepositoryFactoryInformation repositoryFactoryInformation = factory.getBean(repositoryBeanName, RepositoryFactoryInformation.class);
            Class<?> userDomainType = ClassUtils.getUserClass(repositoryFactoryInformation.getRepositoryInformation().getDomainType());

            repositoryFactoryInfos().put(userDomainType, repositoryFactoryInformation);
            repositoryBeanNames().put(userDomainType, BeanFactoryUtils.transformedBeanName(repositoryBeanName));
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, String> repositoryBeanNames() {
        return (Map<Class<?>, String>) delegateBeanWrapper.getPropertyValue(REPOSITORY_BEAN_NAMES_FIELD);
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>> repositoryFactoryInfos() {
        return (Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>>) delegateBeanWrapper.getPropertyValue(REPOSITORY_FACTORY_INFOS_FIELD);
    }

    private void emptyRepositoryFactoryInfosCache() {
        this.delegateBeanWrapper.setPropertyValue(REPOSITORY_FACTORY_INFOS_FIELD, new HashMap<Class<?>, RepositoryFactoryInformation<Object, Serializable>>());
    }

    private void emptyRepositoryBeanNames() {
        this.delegateBeanWrapper.setPropertyValue(REPOSITORY_BEAN_NAMES_FIELD, new HashMap<Class<?>, String>());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}