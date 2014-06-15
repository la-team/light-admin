package org.lightadmin.core.config.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.rest.DomainRepositoryEventListener;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.invoke.DynamicRepositoryInvokerFactory;
import org.springframework.data.rest.core.invoke.RepositoryInvokerFactory;
import org.springframework.data.rest.webmvc.ConfigurationHandlerMethodArgumentResolver;
import org.springframework.data.rest.webmvc.DynamicPersistentEntityResourceProcessor;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.jackson.DynamicPersistentEntityJackson2Module;
import org.springframework.data.rest.webmvc.support.Projector;
import org.springframework.util.ClassUtils;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static org.springframework.beans.PropertyAccessorFactory.forDirectFieldAccess;

@Configuration
public class LightAdminRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

    @Autowired
    private Validator validator;

    @Autowired
    private LightAdminConfiguration lightAdminConfiguration;

    @Autowired
    private GlobalAdministrationConfiguration globalAdministrationConfiguration;

    @Bean
    @Autowired
    public DynamicPersistentEntityResourceProcessor dynamicPersistentEntityResourceProcessor(GlobalAdministrationConfiguration globalAdministrationConfiguration) {
        return new DynamicPersistentEntityResourceProcessor(globalAdministrationConfiguration);
    }

    @Bean
    public Repositories repositories() {
        return configureRepositories(super.repositories());
    }

    @Bean
    public RepositoryInvokerFactory repositoryInvokerFactory() {
        return new DynamicRepositoryInvokerFactory(repositories(), defaultConversionService());
    }

    @Bean
    public ConfigurationHandlerMethodArgumentResolver configurationHandlerMethodArgumentResolver() {
        return new ConfigurationHandlerMethodArgumentResolver(globalAdministrationConfiguration, resourceMetadataHandlerMethodArgumentResolver());
    }

    @Bean
    public DomainRepositoryEventListener domainRepositoryEventListener() {
        return new DomainRepositoryEventListener();
    }

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setDefaultPageSize(10);
        config.setBaseUri(lightAdminConfiguration.getApplicationRestBaseUrl());
        config.exposeIdsFor(globalAdministrationConfiguration.getAllDomainTypesAsArray());
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
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);
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

    private Repositories configureRepositories(Repositories repositories) {
        ConfigurablePropertyAccessor configurablePropertyAccessor = forDirectFieldAccess(repositories);
        ListableBeanFactory beanFactory = (ListableBeanFactory) configurablePropertyAccessor.getPropertyValue("beanFactory");
        configurablePropertyAccessor.setPropertyValue("repositoryBeanNames", repositoryBeanNames(beanFactory));
        configurablePropertyAccessor.setPropertyValue("repositoryFactoryInfos", repositoryFactoryInfos(beanFactory));
        return repositories;
    }

    private Map<Class<?>, String> repositoryBeanNames(ListableBeanFactory beanFactory) {
        Map<Class<?>, String> repositoryBeanNames = newHashMap();
        for (String name : beanFactory.getBeanNamesForType(RepositoryFactoryInformation.class, false, false)) {
            RepositoryFactoryInformation repositoryFactoryInformation = beanFactory.getBean(name, RepositoryFactoryInformation.class);
            Class<?> userDomainType = ClassUtils.getUserClass(repositoryFactoryInformation.getRepositoryInformation().getDomainType());
            repositoryBeanNames.put(userDomainType, BeanFactoryUtils.transformedBeanName(name));
        }
        return repositoryBeanNames;
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>> repositoryFactoryInfos(ListableBeanFactory beanFactory) {
        Map<Class<?>, RepositoryFactoryInformation<Object, Serializable>> repositoryFactoryInfos = newHashMap();
        for (String name : beanFactory.getBeanNamesForType(RepositoryFactoryInformation.class, false, false)) {
            RepositoryFactoryInformation repositoryFactoryInformation = beanFactory.getBean(name, RepositoryFactoryInformation.class);
            Class<?> userDomainType = ClassUtils.getUserClass(repositoryFactoryInformation.getRepositoryInformation().getDomainType());
            repositoryFactoryInfos.put(userDomainType, repositoryFactoryInformation);
        }
        return repositoryFactoryInfos;
    }
}