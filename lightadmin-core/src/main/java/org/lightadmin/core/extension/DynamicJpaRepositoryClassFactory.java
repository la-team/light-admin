package org.lightadmin.core.extension;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;

import java.io.Serializable;

public interface DynamicJpaRepositoryClassFactory {

    <T, ID extends Serializable> Class<? extends DynamicJpaRepository<?, ? extends Serializable>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType);
}