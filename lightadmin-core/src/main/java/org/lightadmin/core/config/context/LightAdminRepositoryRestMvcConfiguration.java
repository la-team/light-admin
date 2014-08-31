package org.lightadmin.core.config.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.bootstrap.RepositoriesFactoryBean;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.rest.DomainRepositoryEventListener;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.invoke.DynamicRepositoryInvokerFactory;
import org.springframework.data.rest.core.invoke.RepositoryInvokerFactory;
import org.springframework.data.rest.core.support.DomainObjectMerger;
import org.springframework.data.rest.webmvc.ConfigurationHandlerMethodArgumentResolver;
import org.springframework.data.rest.webmvc.DomainTypeToJsonMetadataConverter;
import org.springframework.data.rest.webmvc.DynamicDomainObjectMerger;
import org.springframework.data.rest.webmvc.DynamicPersistentEntityResourceProcessor;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.jackson.LightAdminJacksonModule;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

@Configuration
public class LightAdminRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

    @Autowired
    private ListableBeanFactory beanFactory;

    @Bean
    public DynamicPersistentEntityResourceProcessor dynamicPersistentEntityResourceProcessor() {
        return new DynamicPersistentEntityResourceProcessor(globalAdministrationConfiguration(), lightAdminConfiguration());
    }

    @Bean
    public DomainTypeToJsonMetadataConverter domainTypeToJsonMetadataConverter() {
        return new DomainTypeToJsonMetadataConverter(globalAdministrationConfiguration(), entityLinks());
    }

    @Bean
    public Repositories repositories() {
        try {
            RepositoriesFactoryBean repositoriesFactoryBean = new RepositoriesFactoryBean(beanFactory);
            repositoriesFactoryBean.setSingleton(false);
            return repositoriesFactoryBean.getObject();
        } catch (Exception e) {
            throw new BeanInstantiationException(Repositories.class, "Repositories bean instantiation problem!", e);
        }
    }

    @Bean
    public DomainObjectMerger domainObjectMerger() throws Exception {
        return new DynamicDomainObjectMerger(repositories(), defaultConversionService());
    }

    @Bean
    public RepositoryInvokerFactory repositoryInvokerFactory() {
        return new DynamicRepositoryInvokerFactory(repositories(), defaultConversionService());
    }

    @Bean
    public ConfigurationHandlerMethodArgumentResolver configurationHandlerMethodArgumentResolver() {
        return new ConfigurationHandlerMethodArgumentResolver(globalAdministrationConfiguration(), resourceMetadataHandlerMethodArgumentResolver());
    }

    @Bean
    public DomainRepositoryEventListener domainRepositoryEventListener() {
        return new DomainRepositoryEventListener();
    }

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setDefaultPageSize(10);
        config.setBaseUri(lightAdminConfiguration().getApplicationRestBaseUrl());
        config.exposeIdsFor(globalAdministrationConfiguration().getAllDomainTypesAsArray());
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);
    }

    @Override
    public RequestMappingHandlerAdapter repositoryExporterHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.repositoryExporterHandlerAdapter();
        configureRepositoryExporterHandlerAdapter(requestMappingHandlerAdapter);
        return requestMappingHandlerAdapter;
    }

    @Override
    protected void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", validator());
        validatingListener.addValidator("beforeSave", validator());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(configurationHandlerMethodArgumentResolver());
    }

    @Override
    protected void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new LightAdminJacksonModule(globalAdministrationConfiguration()));
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

    protected GlobalAdministrationConfiguration globalAdministrationConfiguration() {
        return beanFactory.getBean(GlobalAdministrationConfiguration.class);
    }

    protected Validator validator() {
        return beanFactory.getBean("validator", Validator.class);
    }

    protected LightAdminConfiguration lightAdminConfiguration() {
        return beanFactory.getBean(LightAdminConfiguration.class);
    }
}