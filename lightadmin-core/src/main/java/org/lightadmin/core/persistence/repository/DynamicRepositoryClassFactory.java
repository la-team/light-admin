package org.lightadmin.core.persistence.repository;

import java.io.Serializable;

public interface DynamicRepositoryClassFactory {

    <T, ID extends Serializable> Class<? extends DynamicJpaRepository<T, ID>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType);
}