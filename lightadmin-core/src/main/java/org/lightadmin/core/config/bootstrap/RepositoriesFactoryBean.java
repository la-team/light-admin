package org.lightadmin.core.config.bootstrap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

public class RepositoriesFactoryBean extends AbstractFactoryBean<Repositories> {

    @Override
    public Class<?> getObjectType() {
        return Repositories.class;
    }

    public RepositoriesFactoryBean(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
        setSingleton(false);
    }

    @Override
    protected Repositories createInstance() throws Exception {
        Repositories repositories = BeanUtils.instantiateClass(Repositories.class);
        configureRepositories(repositories);
        return repositories;
    }

    private Repositories configureRepositories(Repositories repositories) {
        ConfigurablePropertyAccessor configurablePropertyAccessor = forDirectFieldAccess(repositories);
        ListableBeanFactory beanFactory = (ListableBeanFactory) getBeanFactory();
        configurablePropertyAccessor.setPropertyValue("beanFactory", beanFactory);
        configurablePropertyAccessor.setPropertyValue("repositoryBeanNames", repositoryBeanNames(beanFactory));
        configurablePropertyAccessor.setPropertyValue("repositoryFactoryInfos", repositoryFactoryInfos(beanFactory));
        return repositories;
    }

    private Map<Class<?>, String> repositoryBeanNames(ListableBeanFactory beanFactory) {
        Map<Class<?>, String> repositoryBeanNames = newHashMap();
        for (String name : beanFactory.getBeanNamesForType(RepositoryFactoryInformation.class, false, false)) {
            RepositoryFactoryInformation repositoryFactoryInformation = beanFactory.getBean(name, RepositoryFactoryInformation.class);
            Class<?> userDomainType = ClassUtils.getUserClass(repositoryFactoryInformation.getRepositoryInformation().getDomainType());
            repositoryBeanNames.put(userDomainType, BeanFactoryUtils.transformedBeanName(name));
        }
        return repositoryBeanNames;
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>> repositoryFactoryInfos(ListableBeanFactory beanFactory) {
        Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>> repositoryFactoryInfos = newHashMap();
        for (String name : beanFactory.getBeanNamesForType(RepositoryFactoryInformation.class, false, false)) {
            RepositoryFactoryInformation repositoryFactoryInformation = beanFactory.getBean(name, RepositoryFactoryInformation.class);
            Class<?> userDomainType = ClassUtils.getUserClass(repositoryFactoryInformation.getRepositoryInformation().getDomainType());
            repositoryFactoryInfos.put(userDomainType, repositoryFactoryInformation);
        }
        return repositoryFactoryInfos;
    }
}