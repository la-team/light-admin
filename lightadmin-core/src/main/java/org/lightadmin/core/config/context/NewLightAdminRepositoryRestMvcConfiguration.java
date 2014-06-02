package org.lightadmin.core.config.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.rest.DomainRepositoryEventListener;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.invoke.DynamicRepositoryInvokerFactory;
import org.springframework.data.rest.core.invoke.RepositoryInvokerFactory;
import org.springframework.data.rest.webmvc.ConfigurationHandlerMethodArgumentResolver;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.jackson.DynamicPersistentEntityJackson2Module;
import org.springframework.data.rest.webmvc.support.Projector;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

@Configuration
public class NewLightAdminRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

    @Autowired
    private LightAdminConfiguration lightAdminConfiguration;

    @Autowired
    private GlobalAdministrationConfiguration globalAdministrationConfiguration;

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setDefaultPageSize(10);
        config.setBaseUri(lightAdminConfiguration.getApplicationRestBaseUrl());
        config.exposeIdsFor(globalAdministrationConfiguration.getAllDomainTypesAsArray());
    }

    @Bean
    public RepositoryInvokerFactory repositoryInvokerFactory() {
        return new DynamicRepositoryInvokerFactory(repositories(), defaultConversionService());
    }

    @Override
    public RequestMappingHandlerAdapter repositoryExporterHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.repositoryExporterHandlerAdapter();
        configureRepositoryExporterHandlerAdapter(requestMappingHandlerAdapter);
        return requestMappingHandlerAdapter;
    }

    @Bean
    public ConfigurationHandlerMethodArgumentResolver configurationHandlerMethodArgumentResolver() {
        return new ConfigurationHandlerMethodArgumentResolver(globalAdministrationConfiguration, resourceMetadataHandlerMethodArgumentResolver());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(configurationHandlerMethodArgumentResolver());
    }

    @Override
    protected void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new DynamicPersistentEntityJackson2Module(globalAdministrationConfiguration, lightAdminConfiguration, config(), simplePersistentEntityResourceAssembler()));
    }

    @Bean
    public DomainRepositoryEventListener domainRepositoryEventListener() {
        return new DomainRepositoryEventListener();
    }

    @SuppressWarnings("unchecked")
    private void configureRepositoryExporterHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        ConfigurablePropertyAccessor configurablePropertyAccessor = forDirectFieldAccess(requestMappingHandlerAdapter);

        List<HandlerMethodArgumentResolver> defaultArgumentResolvers = (List<HandlerMethodArgumentResolver>) configurablePropertyAccessor.getPropertyValue("argumentResolvers");

        List<HandlerMethodArgumentResolver> argumentResolvers = newLinkedList();
        argumentResolvers.addAll(defaultArgumentResolvers);
        argumentResolvers.add(configurationHandlerMethodArgumentResolver());

        configurablePropertyAccessor.setPropertyValue("argumentResolvers", argumentResolvers);
    }

    private PersistentEntityResourceAssembler simplePersistentEntityResourceAssembler() {
        return new PersistentEntityResourceAssembler(repositories(), entityLinks(), new Projector() {
            @Override
            public Object project(Object source) {
                return source;
            }
        });
    }
}