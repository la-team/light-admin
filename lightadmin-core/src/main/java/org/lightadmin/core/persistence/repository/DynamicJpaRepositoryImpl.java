package org.lightadmin.core.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@Transactional( readOnly = true )
class DynamicJpaRepositoryImpl<T, ID extends Serializable> implements DynamicJpaRepository<T, ID> {

	private SimpleJpaRepository<T, ID> simpleJpaRepository;

	public DynamicJpaRepositoryImpl( Class<T> domainClass, EntityManager entityManager ) {
		this.simpleJpaRepository = new SimpleJpaRepository<T, ID>( domainClass, entityManager );
	}

	@Transactional
	public void delete( final ID id ) {
		simpleJpaRepository.delete( id );
	}

	@Transactional
	public void delete( final T entity ) {
		simpleJpaRepository.delete( entity );
	}

	@Transactional
	public void delete( final Iterable<? extends T> entities ) {
		simpleJpaRepository.delete( entities );
	}

	@Transactional
	public void deleteInBatch( final Iterable<T> entities ) {
		simpleJpaRepository.deleteInBatch( entities );
	}

	@Override
	@Transactional
	public void deleteAll() {
		simpleJpaRepository.deleteAll();
	}

	@Override
	@Transactional
	public void deleteAllInBatch() {
		simpleJpaRepository.deleteAllInBatch();
	}

	public T findOne( final ID id ) {
		return simpleJpaRepository.findOne( id );
	}

	public boolean exists( final ID id ) {
		return simpleJpaRepository.exists( id );
	}

	@Override
	public List<T> findAll() {
		return simpleJpaRepository.findAll();
	}

	public List<T> findAll( final Iterable<ID> ids ) {
		return simpleJpaRepository.findAll( ids );
	}

	@Override
	public List<T> findAll( final Sort sort ) {
		return simpleJpaRepository.findAll( sort );
	}

	@Override
	public Page<T> findAll( final Pageable pageable ) {
		return simpleJpaRepository.findAll( pageable );
	}

	public T findOne( final Specification<T> spec ) {
		return simpleJpaRepository.findOne( spec );
	}

	public List<T> findAll( final Specification<T> spec ) {
		return simpleJpaRepository.findAll( spec );
	}

	public Page<T> findAll( final Specification<T> spec, final Pageable pageable ) {
		return simpleJpaRepository.findAll( spec, pageable );
	}

	public List<T> findAll( final Specification<T> spec, final Sort sort ) {
		return simpleJpaRepository.findAll( spec, sort );
	}

	@Override
	public long count() {
		return simpleJpaRepository.count();
	}

	public long count( final Specification<T> spec ) {
		return simpleJpaRepository.count( spec );
	}

	@Transactional
	public <S extends T> S save( final S entity ) {
		return simpleJpaRepository.save( entity );
	}

	@Transactional
	public T saveAndFlush( final T entity ) {
		return simpleJpaRepository.saveAndFlush( entity );
	}

	@Transactional
	public <S extends T> List<S> save( final Iterable<S> entities ) {
		return simpleJpaRepository.save( entities );
	}

	@Override
	@Transactional
	public void flush() {
		simpleJpaRepository.flush();
	}
}