package org.lightadmin.core.config.context;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.StandardLightAdminConfiguration;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationSourceFactory;
import org.lightadmin.core.config.bootstrap.parsing.validation.DomainConfigurationSourceValidatorFactory;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfigurationFactory;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.extension.WebApplicationContextListener;
import org.lightadmin.core.view.LightAdminSpringTilesInitializer;
import org.lightadmin.core.view.SeparateContainerTilesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.rest.webmvc.ServerHttpRequestMethodArgumentResolver;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.SpringBeanPreparerFactory;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.List;

@Configuration
@Import({
        LightAdminDataConfiguration.class,
        LightAdminDynamicRepositoryConfiguration.class,
        LightAdminDomainConfiguration.class,
//        LightAdminRemoteConfiguration.class,
        NewLightAdminRepositoryRestMvcConfiguration.class
//        LightAdminViewConfiguration.class
})
@EnableWebMvc
public class LightAdminContextConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

//    @Autowired
//    private RestConfigurationInitInterceptor restConfigurationInitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
//        registry.addWebRequestInterceptor(restConfigurationInitInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/styles/**").addResourceLocations("classpath:/META-INF/resources/styles/");
        registry.addResourceHandler("/scripts/**").addResourceLocations("classpath:/META-INF/resources/scripts/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/META-INF/resources/images/").setCachePeriod(31556926);
    }

    @Bean
    @Autowired
    public LightAdminConfiguration lightAdminConfiguration(ServletContext servletContext) {
        return new StandardLightAdminConfiguration(servletContext);
    }

    @Bean
    @Autowired
    public WebApplicationContextListener webApplicationContextListener(final GlobalAdministrationConfiguration globalAdministrationConfiguration,
                                                                       final DomainTypeAdministrationConfigurationFactory domainTypeAdministrationConfigurationFactory,
                                                                       final DomainConfigurationSourceFactory domainConfigurationSourceFactory,
                                                                       final DomainConfigurationSourceValidatorFactory configurationSourceValidatorFactory,
                                                                       final LightAdminConfiguration lightAdminConfiguration) {
        return new WebApplicationContextListener(globalAdministrationConfiguration, domainTypeAdministrationConfigurationFactory, domainConfigurationSourceFactory, configurationSourceValidatorFactory, lightAdminConfiguration);
    }

    @Bean
    @Autowired
    public ServletContextResourceLoader servletContextResourceLoader(ServletContext servletContext) {
        return new ServletContextResourceLoader(servletContext);
    }

//    @Bean
//    @Autowired
//    public FileResourceLoader fileResourceLoader(GlobalAdministrationConfiguration globalAdministrationConfiguration, LightAdminConfiguration lightAdminConfiguration) {
//        return new FileResourceLoader(globalAdministrationConfiguration, lightAdminConfiguration);
//    }

    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
        openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactory);
        return openEntityManagerInViewInterceptor;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

//    @Bean
//    @Autowired
//    public ConversionService conversionService(DomainTypeToResourceConverter domainTypeToResourceConverter) {
//        DefaultFormattingConversionService bean = new DefaultFormattingConversionService();
//        bean.addConverter(domainTypeToResourceConverter);
//        return bean;
//    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        ExceptionHandlerExceptionResolver exceptionHandlerResolver = new ExceptionHandlerExceptionResolver();
        exceptionHandlerResolver.setCustomArgumentResolvers(Arrays.<HandlerMethodArgumentResolver>asList(new ServerHttpRequestMethodArgumentResolver()));
        exceptionHandlerResolver.afterPropertiesSet();

        exceptionResolvers.add(exceptionHandlerResolver);
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        validator.afterPropertiesSet();
        return validator;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

//    @Bean
//    public ApplicationController applicationController() {
//        return new ApplicationController();
//    }

    @Bean
    public ViewResolver viewResolver() {
        final UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(SeparateContainerTilesView.class);
        return viewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        final String[] definitions = {"classpath*:META-INF/tiles/**/*.xml"};

        final TilesConfigurer configurer = new TilesConfigurer();
        configurer.setTilesInitializer(lightAdminSpringTilesInitializer(definitions));
        configurer.setDefinitions(definitions);
        configurer.setPreparerFactoryClass(SpringBeanPreparerFactory.class);
        configurer.setCheckRefresh(true);
        return configurer;
    }

    private LightAdminSpringTilesInitializer lightAdminSpringTilesInitializer(String[] definitions) {
        final LightAdminSpringTilesInitializer lightAdminSpringTilesInitializer = new LightAdminSpringTilesInitializer();
        lightAdminSpringTilesInitializer.setCheckRefresh(true);
        lightAdminSpringTilesInitializer.setDefinitions(definitions);
        lightAdminSpringTilesInitializer.setPreparerFactoryClass(SpringBeanPreparerFactory.class);
        return lightAdminSpringTilesInitializer;
    }
}