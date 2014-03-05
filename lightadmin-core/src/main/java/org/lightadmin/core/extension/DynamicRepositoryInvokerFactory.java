package org.lightadmin.core.extension;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.invoke.DefaultRepositoryInvokerFactory;
import org.springframework.data.rest.core.invoke.RepositoryInvoker;

import java.lang.reflect.Method;

import static org.springframework.util.ReflectionUtils.*;

public class DynamicRepositoryInvokerFactory extends DefaultRepositoryInvokerFactory {

    private static final String PREPARE_INVOKERS_METHOD = "prepareInvokers";

    public DynamicRepositoryInvokerFactory(Repositories repositories, ConversionService conversionService) {
        super(repositories, conversionService);
    }

    @Override
    public RepositoryInvoker getInvokerFor(Class<?> domainType) {
        return createInvoker(domainType);
    }

    @SuppressWarnings("unchecked")
    private RepositoryInvoker createInvoker(Class<?> domainType) {
        Method prepareInvokersMethod = findMethod(DefaultRepositoryInvokerFactory.class, PREPARE_INVOKERS_METHOD, Class.class);
        makeAccessible(prepareInvokersMethod);
        return (RepositoryInvoker) invokeMethod(prepareInvokersMethod, this, domainType);
    }
}