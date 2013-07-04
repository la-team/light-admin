package org.lightadmin.core.persistence.repository.support;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;

class DynamicRepositoryMetadata implements RepositoryMetadata {

    private final Class<?> repositoryInterface = DynamicJpaRepository.class;

    private final EntityInformation<?, ? extends Serializable> entityInformation;

    public DynamicRepositoryMetadata(EntityInformation<?, ? extends Serializable> entityInformation) {
        Assert.notNull(entityInformation);
        this.entityInformation = entityInformation;
    }

    @Override
    public Class<? extends Serializable> getIdType() {
        return entityInformation.getIdType();
    }

    @Override
    public Class<?> getDomainType() {
        return entityInformation.getJavaType();
    }

    @Override
    public Class<?> getRepositoryInterface() {
        return repositoryInterface;
    }

    @Override
    public Class<?> getReturnedDomainClass(final Method method) {
        return entityInformation.getJavaType();
    }
}