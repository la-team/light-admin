package org.springframework.data.rest.core.invoke;

import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class DynamicRepositoryInvokerFactory implements RepositoryInvokerFactory {

    private final Repositories repositories;
    private final ConversionService conversionService;
    private final Map<Class<?>, RepositoryInvoker> invokers;

    public DynamicRepositoryInvokerFactory(Repositories repositories, ConversionService conversionService) {
        Assert.notNull(repositories, "Repositories must not be null!");
        Assert.notNull(conversionService, "ConversionService must not be null!");

        this.repositories = repositories;
        this.conversionService = conversionService;
        this.invokers = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    private RepositoryInvoker prepareInvokers(Class<?> domainType) {
        DynamicJpaRepository repository = (DynamicJpaRepository) repositories.getRepositoryFor(domainType);
        RepositoryInformation information = repositories.getRepositoryInformationFor(domainType);

        return new DynamicRepositoryInvokerImpl(repository, information, conversionService);
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