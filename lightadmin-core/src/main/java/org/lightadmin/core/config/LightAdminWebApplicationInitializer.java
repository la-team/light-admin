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
package org.lightadmin.core.config;

import net.sf.ehcache.constructs.web.filter.GzipFilter;
import org.lightadmin.core.config.bootstrap.LightAdminBeanDefinitionRegistryPostProcessor;
import org.lightadmin.core.config.context.LightAdminContextConfiguration;
import org.lightadmin.core.config.context.LightAdminSecurityConfiguration;
import org.lightadmin.core.view.TilesContainerEnrichmentFilter;
import org.lightadmin.core.web.DispatcherRedirectorServlet;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ResourceServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.File;
import java.util.regex.Pattern;

import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.*;
import static org.lightadmin.core.web.util.WebContextUtils.servletContextAttributeName;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@SuppressWarnings("unused")
@Order(LOWEST_PRECEDENCE)
public class LightAdminWebApplicationInitializer implements WebApplicationInitializer {

    private static final Pattern BASE_URL_PATTERN = Pattern.compile("(/)|(/[\\w-]+)+");

    private static final String CHARSET_ENCODING = "UTF-8";

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        if (lightAdminConfigurationNotEnabled(servletContext)) {
            servletContext.log("LightAdmin Web Administration Module is disabled by default. Skipping.");
            return;
        }

        if (notValidBaseUrl(lightAdminBaseUrl(servletContext))) {
            servletContext.log("LightAdmin Web Administration Module's 'baseUrl' property must match " + BASE_URL_PATTERN.pattern() + " pattern. Skipping.");
            return;
        }

        if (notValidFileStorageDirectoryDefined(servletContext)) {
            servletContext.log("LightAdmin Web Administration Module's global file storage directory doesn't exist or not a directory.");
            return;
        }

        registerCusomResourceServlet(servletContext);

        registerLightAdminDispatcher(servletContext);

        if (notRootUrl(lightAdminBaseUrl(servletContext))) {
            registerLightAdminDispatcherRedirector(servletContext);
        }

        registerHiddenHttpMethodFilter(servletContext);

        if (lightAdminSecurityEnabled(servletContext)) {
            registerSpringSecurityFilter(servletContext);
        }

        registerCharsetFilter(servletContext);

        registerTilesDecorationFilter(servletContext);

        registerGZipFilter(servletContext,
                resourceMapping("/styles/*", servletContext),
                resourceMapping("/scripts/*", servletContext)
        );
    }

    private void registerLightAdminDispatcher(final ServletContext servletContext) {
        final AnnotationConfigWebApplicationContext webApplicationContext = lightAdminApplicationContext(servletContext);

        final DispatcherServlet lightAdminDispatcher = new DispatcherServlet(webApplicationContext);

        ServletRegistration.Dynamic lightAdminDispatcherRegistration = servletContext.addServlet(LIGHT_ADMIN_DISPATCHER_NAME, lightAdminDispatcher);
        lightAdminDispatcherRegistration.setLoadOnStartup(3);
        lightAdminDispatcherRegistration.addMapping(dispatcherUrlMapping(lightAdminBaseUrl(servletContext)));
    }

    private void registerLightAdminDispatcherRedirector(final ServletContext servletContext) {
        final DispatcherRedirectorServlet handlerServlet = new DispatcherRedirectorServlet();

        ServletRegistration.Dynamic lightAdminDispatcherRedirectorRegistration = servletContext.addServlet(LIGHT_ADMIN_DISPATCHER_REDIRECTOR_NAME, handlerServlet);
        lightAdminDispatcherRedirectorRegistration.setLoadOnStartup(4);
        lightAdminDispatcherRedirectorRegistration.addMapping(lightAdminBaseUrl(servletContext));
    }

    private void registerCusomResourceServlet(final ServletContext servletContext) {
        final ResourceServlet resourceServlet = new ResourceServlet();
        resourceServlet.setAllowedResources("/WEB-INF/admin/**/*.jsp");
        resourceServlet.setApplyLastModified(true);
        resourceServlet.setContentType("text/html");

        ServletRegistration.Dynamic customResourceServletRegistration = servletContext.addServlet(LIGHT_ADMIN_CUSTOM_RESOURCE_SERVLET_NAME, resourceServlet);
        customResourceServletRegistration.setLoadOnStartup(2);
        customResourceServletRegistration.addMapping(customResourceServletMapping(servletContext));
    }

    private void registerTilesDecorationFilter(final ServletContext servletContext) {
        final String urlMapping = urlMapping(lightAdminBaseUrl(servletContext));

        servletContext.addFilter("lightAdminTilesContainerEnrichmentFilter", TilesContainerEnrichmentFilter.class).addMappingForUrlPatterns(null, false, urlMapping);
    }

    private void registerHiddenHttpMethodFilter(final ServletContext servletContext) {
        final String urlMapping = urlMapping(lightAdminBaseUrl(servletContext));

        servletContext.addFilter("lightAdminHiddenHttpMethodFilter", HiddenHttpMethodFilter.class).addMappingForUrlPatterns(null, false, urlMapping);
    }

    private void registerSpringSecurityFilter(final ServletContext servletContext) {
        final String urlMapping = urlMapping(lightAdminBaseUrl(servletContext));

        servletContext.addFilter("lightAdminSpringSecurityFilterChain", springSecurityFilterChain()).addMappingForUrlPatterns(null, false, urlMapping);
    }

    private void registerCharsetFilter(final ServletContext servletContext) {
        final String urlMapping = urlMapping(lightAdminBaseUrl(servletContext));

        servletContext.addFilter("lightAdminCharsetFilter", characterEncodingFilter()).addMappingForServletNames(null, false, urlMapping);
    }

    private void registerGZipFilter(ServletContext servletContext, String... urlMappings) {
        GzipFilter gzipFilter = new GzipFilter();

        servletContext.addFilter("lightAdminGzipFilter", gzipFilter).addMappingForUrlPatterns(null, false, urlMappings);
    }

    private AnnotationConfigWebApplicationContext lightAdminApplicationContext(final ServletContext servletContext) {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();

        String basePackage = configurationsBasePackage(servletContext);
        webApplicationContext.register(configurations(servletContext));
        webApplicationContext.addBeanFactoryPostProcessor(new LightAdminBeanDefinitionRegistryPostProcessor(basePackage, servletContext));

        webApplicationContext.setDisplayName("LightAdmin WebApplicationContext");
        webApplicationContext.setNamespace("lightadmin");
        return webApplicationContext;
    }

    private Class[] configurations(final ServletContext servletContext) {
        if (lightAdminSecurityEnabled(servletContext)) {
            return new Class[]{LightAdminContextConfiguration.class, LightAdminSecurityConfiguration.class};
        }
        return new Class[]{LightAdminContextConfiguration.class};
    }

    private DelegatingFilterProxy springSecurityFilterChain() {
        final DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");
        securityFilterChain.setContextAttribute(servletContextAttributeName());
        return securityFilterChain;
    }

    private CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(CHARSET_ENCODING);
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    private boolean notValidBaseUrl(String url) {
        return !BASE_URL_PATTERN.matcher(url).matches();
    }

    private String resourceMapping(String resource, ServletContext servletContext) {
        if (rootUrl(lightAdminBaseUrl(servletContext))) {
            return resource;
        }

        return lightAdminBaseUrl(servletContext) + resource;
    }

    private String customResourceServletMapping(ServletContext servletContext) {
        if (rootUrl(lightAdminBaseUrl(servletContext))) {
            return "/custom";
        }

        return lightAdminBaseUrl(servletContext) + "/custom";
    }

    private String urlMapping(String baseUrl) {
        if (rootUrl(baseUrl)) {
            return "/*";
        }
        return baseUrl + "/*";
    }

    private String dispatcherUrlMapping(String url) {
        if (rootUrl(url)) {
            return "/";
        }
        return urlMapping(url);
    }

    private boolean rootUrl(final String url) {
        return "/".equals(url);
    }

    private boolean notRootUrl(final String url) {
        return !rootUrl(url);
    }

    private String configurationsBasePackage(final ServletContext servletContext) {
        return servletContext.getInitParameter(LIGHT_ADMINISTRATION_BASE_PACKAGE);
    }

    private String lightAdminBaseUrl(final ServletContext servletContext) {
        return servletContext.getInitParameter(LIGHT_ADMINISTRATION_BASE_URL);
    }

    private boolean lightAdminSecurityEnabled(final ServletContext servletContext) {
        return toBoolean(servletContext.getInitParameter(LIGHT_ADMINISTRATION_SECURITY));
    }

    private String lightAdminGlobalFileStorageDirectory(final ServletContext servletContext) {
        return servletContext.getInitParameter(LIGHT_ADMINISTRATION_FILE_STORAGE_PATH);
    }

    private boolean lightAdminConfigurationNotEnabled(final ServletContext servletContext) {
        return isBlank(lightAdminBaseUrl(servletContext)) || isBlank(configurationsBasePackage(servletContext));
    }

    private boolean notValidFileStorageDirectoryDefined(final ServletContext servletContext) {
        final String fileStorageDirectoryPath = lightAdminGlobalFileStorageDirectory(servletContext);

        if (isBlank(fileStorageDirectoryPath)) {
            return false;
        }

        final File fileStorageDirectory = getFile(fileStorageDirectoryPath);
        return !fileStorageDirectory.exists() || !fileStorageDirectory.isDirectory();
    }
}