package org.lightadmin.demo;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class DynamicJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements DynamicJpaRepository<T, ID> {

	public DynamicJpaRepositoryImpl( JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager ) {
		super( entityInformation, entityManager );
	}

	public DynamicJpaRepositoryImpl( Class<T> domainClass, EntityManager em ) {
		super( domainClass, em );
	}
}