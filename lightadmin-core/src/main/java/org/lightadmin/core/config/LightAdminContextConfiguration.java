package org.lightadmin.core.config;

import org.lightadmin.core.context.StandardWebContext;
import org.lightadmin.core.context.WebContext;
import org.lightadmin.core.rest.DomainTypeToResourceConverter;
import org.lightadmin.core.rest.RestConfigurationInitInterceptor;
import org.lightadmin.core.view.SeparateContainerTilesView;
import org.lightadmin.core.web.ApplicationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.ServerHttpRequestMethodArgumentResolver;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
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

import static org.apache.commons.lang.BooleanUtils.toBoolean;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_BASE_URL;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.LIGHT_ADMINISTRATION_SECURITY;

@Configuration
@Import({
        LightAdminDataConfiguration.class, LightAdminDomainConfiguration.class, LightAdminRemoteConfiguration.class, LightAdminRepositoryRestConfiguration.class, LightAdminViewConfiguration.class
})
@EnableWebMvc
public class LightAdminContextConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment environment;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private RestConfigurationInitInterceptor restConfigurationInitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
        registry.addWebRequestInterceptor(restConfigurationInitInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/styles/**").addResourceLocations("classpath:/META-INF/resources/styles/").setCachePeriod(31556926);
        registry.addResourceHandler("/scripts/**").addResourceLocations("classpath:/META-INF/resources/scripts/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/META-INF/resources/images/").setCachePeriod(31556926);
    }

    @Bean
    public WebContext lightAdminContext() {
        final String lightAdminBaseUrl = environment.getProperty(LIGHT_ADMINISTRATION_BASE_URL);
        final boolean securityEnabled = toBoolean(environment.getProperty(LIGHT_ADMINISTRATION_SECURITY));

        return new StandardWebContext(lightAdminBaseUrl, securityEnabled);
    }

    @Bean
    @Autowired
    public ServletContextResourceLoader servletContextResourceLoader(ServletContext servletContext) {
        return new ServletContextResourceLoader(servletContext);
    }

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

    @Bean
    @Autowired
    public ConversionService conversionService(DomainTypeToResourceConverter domainTypeToResourceConverter) {
        DefaultFormattingConversionService bean = new DefaultFormattingConversionService();
        bean.addConverter(domainTypeToResourceConverter);
        return bean;
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        ExceptionHandlerExceptionResolver exceptionHandlerResolver = new ExceptionHandlerExceptionResolver();
        exceptionHandlerResolver.setCustomArgumentResolvers(Arrays.<HandlerMethodArgumentResolver>asList(new ServerHttpRequestMethodArgumentResolver()));
        exceptionHandlerResolver.afterPropertiesSet();

        exceptionResolvers.add(exceptionHandlerResolver);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

    @Bean
    public ApplicationController applicationController() {
        return new ApplicationController();
    }

    @Bean
    public ViewResolver viewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(SeparateContainerTilesView.class);
        return viewResolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer configurer = new TilesConfigurer();
        configurer.setDefinitions(new String[]{
                "classpath*:META-INF/tiles/**/*.xml"
        });
        configurer.setPreparerFactoryClass(SpringBeanPreparerFactory.class);
        configurer.setCheckRefresh(true);
        return configurer;
    }
}
