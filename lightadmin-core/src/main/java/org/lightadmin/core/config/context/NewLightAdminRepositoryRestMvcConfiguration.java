package org.lightadmin.core.config.context;

import org.lightadmin.core.extension.DynamicRepositoryBeanFactory;
import org.lightadmin.core.extension.RefreshableRepositories;
import org.lightadmin.core.extension.RefreshableResourceMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.core.EvoInflectorRelProvider;

@Configuration
public class NewLightAdminRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

    @Autowired
    private DynamicRepositoryBeanFactory domainTypeRepositoryBeanFactory;

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
//        config.setResourceMappingForDomainType(null);
//        config.setResourceMappingForRepository(null);
    }

    @Override
    @Bean
    public Repositories repositories() {
        return new RefreshableRepositories(domainTypeRepositoryBeanFactory);
    }

    @Override
    @Bean
    public ResourceMappings resourceMappings() {
        Repositories repositories = repositories();
        RepositoryRestConfiguration config = config();

        return new RefreshableResourceMappings(config, repositories, new EvoInflectorRelProvider());
    }
}