package org.lightadmin.core.persistence.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@Transactional(readOnly = true)
class DynamicJpaRepositoryImpl<T, ID extends Serializable> implements DynamicJpaRepository<T, ID> {

    private static final Logger log = LoggerFactory.getLogger(DynamicJpaRepositoryImpl.class);

    private final SimpleJpaRepository<T, ID> simpleJpaRepository;
    private T nullPlaceholder;

    public DynamicJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        this.simpleJpaRepository = new SimpleJpaRepository<T, ID>(domainClass, entityManager);
        try {
            this.nullPlaceholder = BeanUtils.instantiateClass(domainClass);
        } catch (BeanInstantiationException e) {
            log.info("Cannot create NULL placeholder for type {}", domainClass.getSimpleName());
        }
    }

    @Override
    @Transactional
    public void delete(final ID id) {
        simpleJpaRepository.delete(id);
    }

    @Override
    @Transactional
    public void delete(final T entity) {
        simpleJpaRepository.delete(entity);
    }

    @Override
    @Transactional
    public void delete(final Iterable<? extends T> entities) {
        simpleJpaRepository.delete(entities);
    }

    @Override
    @Transactional
    public void deleteInBatch(final Iterable<T> entities) {
        simpleJpaRepository.deleteInBatch(entities);
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

    @Override
    public T findOne(final ID id) {
        if (id == null) {
            return null;
        }
        return simpleJpaRepository.findOne(id);
    }

    @Override
    public boolean exists(final ID id) {
        return simpleJpaRepository.exists(id);
    }

    @Override
    public List<T> findAll() {
        return simpleJpaRepository.findAll();
    }

    @Override
    public List<T> findAll(final Iterable<ID> ids) {
        return simpleJpaRepository.findAll(ids);
    }

    @Override
    public List<T> findAll(final Sort sort) {
        return simpleJpaRepository.findAll(sort);
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        return simpleJpaRepository.findAll(pageable);
    }

    @Override
    public T findOne(final Specification<T> spec) {
        return simpleJpaRepository.findOne(spec);
    }

    @Override
    public List<T> findAll(final Specification<T> spec) {
        return simpleJpaRepository.findAll(spec);
    }

    @Override
    public Page<T> findAll(final Specification<T> spec, final Pageable pageable) {
        return simpleJpaRepository.findAll(spec, pageable);
    }

    @Override
    public List<T> findAll(final Specification<T> spec, final Sort sort) {
        return simpleJpaRepository.findAll(spec, sort);
    }

    @Override
    public long count() {
        return simpleJpaRepository.count();
    }

    @Override
    public long count(final Specification<T> spec) {
        return simpleJpaRepository.count(spec);
    }

    @Override
    @Transactional
    public <S extends T> S save(final S entity) {
        return simpleJpaRepository.save(entity);
    }

    @Override
    @Transactional
    public T saveAndFlush(final T entity) {
        return simpleJpaRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public <S extends T> List<S> save(final Iterable<S> entities) {
        return simpleJpaRepository.save(entities);
    }

    @Override
    @Transactional
    public void flush() {
        simpleJpaRepository.flush();
    }

    @Override
    public T getNullPlaceholder() {
        return nullPlaceholder;
    }

    @Override
    public boolean isNullPlaceholder(Object val) {
        return (val == nullPlaceholder);
    }

}
