package org.lightadmin.core.extension;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;

import java.io.Serializable;

public interface DynamicRepositoryClassFactory {

    <T, ID extends Serializable> Class<? extends DynamicJpaRepository<T, ID>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType);
}