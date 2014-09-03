/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.persistence.repository.invoker;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.core.invoke.RepositoryInvoker;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class DynamicRepositoryInvokerWrapper implements DynamicRepositoryInvoker {

    private final DynamicJpaRepository<?, ?> repository;
    private final RepositoryInvoker repositoryInvoker;

    public DynamicRepositoryInvokerWrapper(DynamicJpaRepository dynamicRepository, RepositoryInvoker repositoryInvoker) {
        this.repository = dynamicRepository;
        this.repositoryInvoker = repositoryInvoker;
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