package org.lightadmin.core.extension;

import org.springframework.data.repository.core.support.RepositoryFactoryInformation;

public interface RepositoryFactoryBeanCreator {

    RepositoryFactoryInformation createRepositoryFactoryBean(Class<?> domainType);
}