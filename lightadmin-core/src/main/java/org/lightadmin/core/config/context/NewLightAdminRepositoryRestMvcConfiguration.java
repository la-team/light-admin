package org.lightadmin.core.config.context;

import org.lightadmin.core.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.UriToEntityConverter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.invoke.RepositoryInvokerFactory;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.core.EvoInflectorRelProvider;

@Configuration
public class NewLightAdminRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

    @Autowired
    private DynamicRepositoryBeanFactory domainTypeRepositoryBeanFactory;

    @Bean
    public UriToEntityConverter uriToEntityConverter() {
        return new RefreshableUriToEntityConverter(repositories(), domainClassConverter());
    }

    @Override
    @Bean
    public Repositories repositories() {
        return new RefreshableRepositories(domainTypeRepositoryBeanFactory);
    }

    @Override
    @Bean
    public RepositoryInvokerFactory repositoryInvokerFactory() {
        return new DynamicRepositoryInvokerFactory(repositories(), defaultConversionService());
    }

    @Override
    @Bean
    public ResourceMappings resourceMappings() {
        Repositories repositories = repositories();
        RepositoryRestConfiguration config = config();

        return new RefreshableResourceMappings(config, repositories, new EvoInflectorRelProvider());
    }
}