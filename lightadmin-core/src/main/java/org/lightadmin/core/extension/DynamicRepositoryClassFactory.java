package org.lightadmin.core.extension;

import org.springframework.data.repository.Repository;

import java.io.Serializable;

public interface DynamicRepositoryClassFactory {

    <T, ID extends Serializable> Class<? extends Repository<T, ID>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType);
}