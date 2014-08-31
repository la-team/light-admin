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
package org.springframework.data.rest.core.invoke;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.Assert;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class DynamicRepositoryInvokerFactory implements RepositoryInvokerFactory {

    private final Repositories repositories;
    private final ConversionService conversionService;
    private final Map<Class<?>, RepositoryInvoker> invokers;

    public DynamicRepositoryInvokerFactory(Repositories repositories, ConversionService conversionService) {
        Assert.notNull(repositories, "Repositories must not be null!");
        Assert.notNull(conversionService, "ConversionService must not be null!");

        this.repositories = repositories;
        this.conversionService = conversionService;
        this.invokers = newHashMap();
    }

    @SuppressWarnings("unchecked")
    private RepositoryInvoker prepareInvokers(Class<?> domainType) {
        DynamicJpaRepository repository = (DynamicJpaRepository) repositories.getRepositoryFor(domainType);
        RepositoryInformation information = repositories.getRepositoryInformationFor(domainType);

        return new DynamicRepositoryInvokerWrapper(repository, information, conversionService);
    }

    @Override
    public RepositoryInvoker getInvokerFor(Class<?> domainType) {
        RepositoryInvoker invoker = invokers.get(domainType);

        if (invoker != null) {
            return invoker;
        }

        invoker = prepareInvokers(domainType);
        invokers.put(domainType, invoker);

        return invoker;
    }
}