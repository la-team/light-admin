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
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.invoke.RepositoryInvoker;
import org.springframework.data.rest.core.invoke.RepositoryInvokerFactory;

public class DynamicRepositoryInvokerFactory implements RepositoryInvokerFactory {

    private final RepositoryInvokerFactory delegate;
    private final Repositories repositories;

    public DynamicRepositoryInvokerFactory(Repositories repositories, RepositoryInvokerFactory repositoryInvokerFactory) {
        this.repositories = repositories;
        this.delegate = repositoryInvokerFactory;
    }

    @Override
    public RepositoryInvoker getInvokerFor(Class<?> domainType) {
        DynamicJpaRepository dynamicJpaRepository = (DynamicJpaRepository) repositories.getRepositoryFor(domainType);
        RepositoryInvoker repositoryInvoker = delegate.getInvokerFor(domainType);

        return new DynamicRepositoryInvokerWrapper(dynamicJpaRepository, repositoryInvoker);
    }
}