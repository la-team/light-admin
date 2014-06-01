package org.lightadmin.core.config.context;

//import org.lightadmin.core.config.LightAdminConfiguration;
//import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
//import org.lightadmin.core.rest.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
//import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
//import org.springframework.data.rest.repository.UriToDomainObjectUriResolver;
//import org.springframework.data.rest.repository.context.ValidatingRepositoryEventListener;
//import org.springframework.data.rest.webmvc.*;
//import org.springframework.data.rest.webmvc.json.JsonSchemaController;
//import org.springframework.data.rest.webmvc.json.RepositoryAwareJacksonModule;
//import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
//import org.springframework.validation.Validator;
//import org.springframework.web.servlet.mvc.method.annotation.ServletResponseMethodArgumentResolver;

@Deprecated
@Configuration
public class LightAdminRepositoryRestConfiguration {

//    @Bean
//    public DomainRepositoryEventListener domainRepositoryEventListener() {
//        return new DomainRepositoryEventListener();
//    }
//
//    @Bean
//    @Autowired
//    public CustomAnnotatedHandlerRepositoryEventListener customAnnotatedHandlerRepositoryEventListener(LightAdminConfiguration lightAdminContext, AutowireCapableBeanFactory beanFactory) {
//        return new CustomAnnotatedHandlerRepositoryEventListener(lightAdminContext, beanFactory);
//    }
//
//    @Bean
//    public DynamicJpaRepositoryExporter jpaRepositoryExporter() {
//        return new DynamicJpaRepositoryExporter();
//    }
//
//    @Bean
//    public DynamicRepositoryRestController repositoryRestController() throws Exception {
//        return new DynamicRepositoryRestController();
//    }
//
//    @Bean
//    public PagingAndSortingMethodArgumentResolver pagingAndSortingMethodArgumentResolver() {
//        return new DynamicPagingAndSortingMethodArgumentResolver();
//    }
//
//    @Bean
//    @Autowired
//    public DomainTypeToResourceConverter domainTypeToResourceConverter(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminContext) {
//        return new DomainTypeToResourceConverter(configuration, lightAdminContext, repositoryRestConfiguration());
//    }
//
//    @Bean
//    @Autowired
//    public DomainTypeResourceModule domainTypeResourceModule(DomainTypeToResourceConverter domainTypeToResourceConverter) {
//        return new DomainTypeResourceModule(domainTypeToResourceConverter);
//    }
//
//    @Bean
//    public HttpMessageConverterRefresher httpMessageConverterRefresher() {
//        return new HttpMessageConverterRefresher();
//    }
//
//    @Bean
//    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
//        return new PersistenceAnnotationBeanPostProcessor();
//    }
//
//    /**
//     * Use the pre-defined {@link org.springframework.data.rest.repository.context.ValidatingRepositoryEventListener} defined by the user or create a default one.
//     */
//    @Bean
//    @Autowired
//    public ValidatingRepositoryEventListener validatingRepositoryEventListener(Validator validator) {
//        return new ValidatingRepositoryEventListener().addValidator("beforeSave", validator);
//    }
//
//    /**
//     * A special Jackson {@link org.codehaus.jackson.map.Module} implementation that configures converters for entities.
//     */
//    @Bean
//    public RepositoryAwareJacksonModule jacksonModule() {
//        return new RepositoryAwareJacksonModule();
//    }
//
//    /**
//     * Special Repository-aware {@link org.springframework.http.converter.HttpMessageConverter} that can deal with
//     * entities and links.
//     */
//    @Bean
//    public RepositoryAwareMappingHttpMessageConverter mappingHttpMessageConverter() {
//        return new RepositoryAwareMappingHttpMessageConverter();
//    }
//
//    /**
//     * A {@link org.springframework.data.rest.core.UriResolver} implementation that takes a {@link java.net.URI} and
//     * turns it into a top-level domain object.
//     */
//    @Bean
//    public UriToDomainObjectUriResolver domainObjectResolver() {
//        return new UriToDomainTypeResolver();
//    }
//
//    @Bean
//    public JsonSchemaController jsonSchemaController() {
//        return new JsonSchemaController();
//    }
//
//    @Bean
//    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
//        return new BaseUriMethodArgumentResolver();
//    }
//
//    @Bean
//    public ServerHttpRequestMethodArgumentResolver serverHttpRequestMethodArgumentResolver() {
//        return new ServerHttpRequestMethodArgumentResolver();
//    }
//
//    @Bean
//    public ServletResponseMethodArgumentResolver servletResponseMethodArgumentResolver() {
//        return new ServletResponseMethodArgumentResolver();
//    }
//
//    /**
//     * Special {@link org.springframework.web.servlet.HandlerAdapter} that only recognizes handler methods defined in the
//     * {@link RepositoryRestController} class.
//     */
//    @Bean
//    public RepositoryRestHandlerAdapter repositoryExporterHandlerAdapter() {
//        return new RepositoryRestHandlerAdapter();
//    }
//
//    /**
//     * Special {@link org.springframework.web.servlet.HandlerMapping} that only recognizes handler methods defined in the
//     * {@link RepositoryRestController} class.
//     */
//    @Bean
//    public RepositoryRestHandlerMapping repositoryExporterHandlerMapping() {
//        return new RepositoryRestHandlerMapping();
//    }
}