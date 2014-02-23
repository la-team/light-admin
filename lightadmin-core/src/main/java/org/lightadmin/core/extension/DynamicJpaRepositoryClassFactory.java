package org.lightadmin.core.extension;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface DynamicJpaRepositoryClassFactory {

    <T, ID extends Serializable> Class<? extends JpaRepository<T, ID>> createDynamicRepositoryClass(Class<T> domainType, Class<ID> idType);
}