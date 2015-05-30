/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.config.context;

import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.StandardLightAdminConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.storage.FileResourceStorage;
import org.lightadmin.core.storage.LightAdminFileResourceStorage;
import org.lightadmin.core.view.LightAdminSpringTilesInitializer;
import org.lightadmin.core.view.LightAdminTilesView;
import org.lightadmin.core.web.ApplicationController;
import org.lightadmin.core.web.support.FileResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.rest.webmvc.ServerHttpRequestMethodArgumentResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.view.tiles3.SpringBeanPreparerFactory;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.servlet.DispatcherServlet.VIEW_RESOLVER_BEAN_NAME;

@Configuration
@Import({
        LightAdminDataConfiguration.class,
        LightAdminDomainConfiguration.class,
        LightAdminRemoteConfiguration.class,
        LightAdminRepositoryRestMvcConfiguration.class,
        LightAdminViewConfiguration.class
})
@EnableWebMvc
public class LightAdminContextConfiguration extends WebMvcConfigurerAdapter {

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
    public ServletContextResourceLoader servletContextResourceLoader(ServletContext servletContext) {
        return new ServletContextResourceLoader(servletContext);
    }

    @Bean
    @Autowired
    public FileResourceStorage fileResourceStorage(GlobalAdministrationConfiguration globalAdministrationConfiguration, LightAdminConfiguration lightAdminConfiguration) {
        return new LightAdminFileResourceStorage(globalAdministrationConfiguration, lightAdminConfiguration);
    }

    @Bean
    @Autowired
    public FileResourceLoader fileResourceLoader(GlobalAdministrationConfiguration globalAdministrationConfiguration, FileResourceStorage fileResourceStorage) {
        return new FileResourceLoader(globalAdministrationConfiguration, fileResourceStorage);
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

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

    @Bean
    public ApplicationController applicationController() {
        return new ApplicationController();
    }

    @Bean(name = VIEW_RESOLVER_BEAN_NAME)
    public ViewResolver viewResolver() {
        return new TilesViewResolver() {
            @Override
            protected Class<?> requiredViewClass() {
                return LightAdminTilesView.class;
            }
        };
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        final String[] definitions = {"classpath*:META-INF/tiles/definitions.xml"};

        final TilesConfigurer configurer = new TilesConfigurer();
        configurer.setTilesInitializer(lightAdminSpringTilesInitializer(definitions));
        configurer.setDefinitions(definitions);
        configurer.setPreparerFactoryClass(SpringBeanPreparerFactory.class);
        return configurer;
    }

    private LightAdminSpringTilesInitializer lightAdminSpringTilesInitializer(String[] definitions) {
        return new LightAdminSpringTilesInitializer(definitions);
    }
}