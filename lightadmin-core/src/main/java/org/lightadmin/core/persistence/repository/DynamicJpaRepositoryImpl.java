package org.lightadmin.core.persistence.repository;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class DynamicJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements DynamicJpaRepository<T, ID> {

	public DynamicJpaRepositoryImpl( Class<T> domainClass, EntityManager em ) {
		super( domainClass, em );
	}
}