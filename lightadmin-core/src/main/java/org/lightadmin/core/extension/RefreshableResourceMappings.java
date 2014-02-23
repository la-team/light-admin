package org.lightadmin.core.extension;

import org.lightadmin.core.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMapping;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.hateoas.RelProvider;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.springframework.util.ReflectionUtils.*;

public class RefreshableResourceMappings extends ResourceMappings implements Refreshable, ApplicationListener<RepositoriesRefreshedEvent> {

    private static final String CACHE_FIELD = "cache";
    private static final String SEARCH_CACHE_FIELD = "searchCache";
    private static final String PROPERTY_CACHE_FIELD = "propertyCache";
    private static final String REPOSITORIES_FIELD = "repositories";

    private static final String POPULATE_CACHE_METHOD = "populateCache";

    private final BeanWrapper delegateBeanWrapper;

    public RefreshableResourceMappings(RepositoryRestConfiguration config, Repositories repositories) {
        super(config, repositories);

        this.delegateBeanWrapper = new DirectFieldAccessFallbackBeanWrapper(this);
    }

    public RefreshableResourceMappings(RepositoryRestConfiguration config, Repositories repositories, RelProvider relProvider) {
        super(config, repositories, relProvider);

        this.delegateBeanWrapper = new DirectFieldAccessFallbackBeanWrapper(this);
    }

    @Override
    public void refresh() {
        emptyCache();
        emptySearchCache();
        emptyPropertyCache();

        repopulateCache(repositories());
    }

    @Override
    public void onApplicationEvent(RepositoriesRefreshedEvent event) {
        refresh();
    }

    private void emptyCache() {
        this.delegateBeanWrapper.setPropertyValue(CACHE_FIELD, new HashMap<Class<?>, ResourceMetadata>());
    }

    private void emptySearchCache() {
        this.delegateBeanWrapper.setPropertyValue(SEARCH_CACHE_FIELD, new HashMap<Class<?>, SearchResourceMappings>());
    }

    private void emptyPropertyCache() {
        this.delegateBeanWrapper.setPropertyValue(PROPERTY_CACHE_FIELD, new HashMap<PersistentProperty<?>, ResourceMapping>());
    }

    private Repositories repositories() {
        return (Repositories) this.delegateBeanWrapper.getPropertyValue(REPOSITORIES_FIELD);
    }

    private void repopulateCache(Repositories repositories) {
        Method populateCacheMethod = findMethod(ResourceMappings.class, POPULATE_CACHE_METHOD, Repositories.class);
        makeAccessible(populateCacheMethod);
        invokeMethod(populateCacheMethod, this, repositories);
    }
}