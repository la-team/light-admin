package org.springframework.data.rest.core.invoke;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.core.RepositoryInformation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class DynamicRepositoryInvokerImpl implements DynamicRepositoryInvoker {

    private DynamicJpaRepository<?, ?> repository;
    private RepositoryInvoker repositoryInvoker;

    public DynamicRepositoryInvokerImpl(DynamicJpaRepository<Object, Serializable> repository, RepositoryInformation information, ConversionService conversionService) {
        this.repository = repository;
        this.repositoryInvoker = new PagingAndSortingRepositoryInvoker(repository, information, conversionService);
    }

    @Override
    public Page findAll(Specification spec, Pageable pageable) {
        return repository.findAll(spec, pageable);
    }

    @Override
    public List findAll(Specification spec, Sort sort) {
        return repository.findAll(spec, sort);
    }

    @Override
    public List findAll(Specification spec) {
        return repository.findAll(spec);
    }

    @Override
    public long count(Specification spec) {
        return repository.count(spec);
    }

    @Override
    public <T> T invokeSave(T object) {
        return repositoryInvoker.invokeSave(object);
    }

    @Override
    public <T> T invokeFindOne(Serializable id) {
        return repositoryInvoker.invokeFindOne(id);
    }

    @Override
    public Iterable<Object> invokeFindAll(Pageable pageable) {
        return repositoryInvoker.invokeFindAll(pageable);
    }

    @Override
    public Iterable<Object> invokeFindAll(Sort sort) {
        return repositoryInvoker.invokeFindAll(sort);
    }

    @Override
    public void invokeDelete(Serializable serializable) {
        repositoryInvoker.invokeDelete(serializable);
    }

    @Override
    public Object invokeQueryMethod(Method method, Map<String, String[]> parameters, Pageable pageable, Sort sort) {
        return repositoryInvoker.invokeQueryMethod(method, parameters, pageable, sort);
    }

    @Override
    public boolean hasSaveMethod() {
        return repositoryInvoker.hasSaveMethod();
    }

    @Override
    public boolean exposesSave() {
        return repositoryInvoker.exposesSave();
    }

    @Override
    public boolean hasDeleteMethod() {
        return repositoryInvoker.hasDeleteMethod();
    }

    @Override
    public boolean exposesDelete() {
        return repositoryInvoker.exposesDelete();
    }

    @Override
    public boolean hasFindOneMethod() {
        return repositoryInvoker.hasFindOneMethod();
    }

    @Override
    public boolean exposesFindOne() {
        return repositoryInvoker.exposesFindOne();
    }

    @Override
    public boolean hasFindAllMethod() {
        return repositoryInvoker.hasFindAllMethod();
    }

    @Override
    public boolean exposesFindAll() {
        return repositoryInvoker.exposesFindAll();
    }
}