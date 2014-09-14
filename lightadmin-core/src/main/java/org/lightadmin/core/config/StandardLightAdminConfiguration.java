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

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.URI;

import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.*;

public class StandardLightAdminConfiguration implements LightAdminConfiguration {

    private final String applicationBaseUrl;
    private final String applicationBaseNoEndSeparator;
    private final boolean securityEnabled;
    private final String securityLogoutUrl;
    private final String backToSiteUrl;
    private final String helpUrl;
    private final File fileStorageDirectory;
    private final boolean fileStreaming;
    private final String basePackage;
    private final boolean demoMode;

    public StandardLightAdminConfiguration(ServletContext servletContext) {
        this.basePackage = servletContext.getInitParameter(LIGHT_ADMINISTRATION_BASE_PACKAGE);

        this.applicationBaseUrl = servletContext.getInitParameter(LIGHT_ADMINISTRATION_BASE_URL);
        this.applicationBaseNoEndSeparator = urlWithoutEndSeparator(this.applicationBaseUrl);
        this.backToSiteUrl = backToSiteUrl(servletContext);
        this.helpUrl = helpUrl(servletContext);

        this.fileStorageDirectory = fileStorageDirectory(servletContext);
        this.fileStreaming = BooleanUtils.toBoolean(servletContext.getInitParameter(LIGHT_ADMINISTRATION_FILE_STREAMING));

        this.demoMode = BooleanUtils.toBoolean(servletContext.getInitParameter(LIGHT_ADMINISTRATION_DEMO_MODE));

        this.securityEnabled = BooleanUtils.toBoolean(servletContext.getInitParameter(LIGHT_ADMINISTRATION_SECURITY));
        if (securityEnabled) {
            this.securityLogoutUrl = servletContext.getContextPath() + this.applicationBaseNoEndSeparator + LIGHT_ADMIN_SECURITY_LOGOUT_URL_DEFAULT;
        } else {
            this.securityLogoutUrl = servletContext.getContextPath() + defaultIfBlank(servletContext.getInitParameter(LIGHT_ADMINISTRATION_SECURITY_LOGOUT_URL), "#");
        }
    }

    @Override
    public URI getApplicationRestBaseUrl() {
        return UriComponentsBuilder.fromUriString(LIGHT_ADMIN_REST_URL_DEFAULT).build().toUri();
    }

    @Override
    public String getBasePackage() {
        return this.basePackage;
    }

    @Override
    public File getFileStorageDirectory() {
        return fileStorageDirectory;
    }

    @Override
    public boolean isFileStreamingEnabled() {
        return fileStreaming;
    }

    @Override
    public String getBackToSiteUrl() {
        return backToSiteUrl;
    }

    @Override
    public String getHelpUrl() {
        return helpUrl;
    }

    @Override
    public String getApplicationBaseUrl() {
        return applicationBaseUrl;
    }

    @Override
    public String getApplicationUrl(String path) {
        return applicationBaseNoEndSeparator + path;
    }

    @Override
    public boolean isSecurityEnabled() {
        return securityEnabled;
    }

    public boolean isDemoMode() {
        return demoMode;
    }

    @Override
    public String getSecurityLogoutUrl() {
        return securityLogoutUrl;
    }

    private File fileStorageDirectory(ServletContext servletContext) {
        final String fileStoragePath = servletContext.getInitParameter(LIGHT_ADMINISTRATION_FILE_STORAGE_PATH);

        return isBlank(fileStoragePath) ? null : getFile(fileStoragePath);
    }

    private String backToSiteUrl(ServletContext servletContext) {
        final String backToSiteUrl = servletContext.getInitParameter(LIGHT_ADMINISTRATION_BACK_TO_SITE_URL);

        return isBlank(backToSiteUrl) ? LIGHT_ADMINISTRATION_BACK_TO_SITE_DEFAULT_URL : backToSiteUrl;
    }

    private String helpUrl(ServletContext servletContext) {
        final String helpUrl = servletContext.getInitParameter(LIGHT_ADMINISTRATION_HELP_URL);

        return isBlank(helpUrl) ? LIGHT_ADMINISTRATION_HELP_DEFAULT_URL : helpUrl;
    }

    private String urlWithoutEndSeparator(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}