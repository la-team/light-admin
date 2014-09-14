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
package org.lightadmin.api.config;

import javax.servlet.ServletContext;

import static org.lightadmin.core.util.LightAdminConfigurationUtils.*;

@SuppressWarnings("unused")
public class LightAdmin {

    private final ServletContext servletContext;

    private LightAdmin(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public static LightAdmin configure(ServletContext servletContext) {
        return new LightAdmin(servletContext);
    }

    public LightAdmin basePackage(String basePackage) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_BASE_PACKAGE, basePackage);
        return this;
    }

    public LightAdmin baseUrl(String baseUrl) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_BASE_URL, baseUrl);
        return this;
    }

    public LightAdmin security(boolean security) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_SECURITY, Boolean.toString(security));
        return this;
    }

    public LightAdmin securityLogoutUrl(String securityLogoutUrl) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_SECURITY_LOGOUT_URL, securityLogoutUrl);
        return this;
    }

    public LightAdmin backToSiteUrl(String backToSiteUrl) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_BACK_TO_SITE_URL, backToSiteUrl);
        return this;
    }

    public LightAdmin helpUrl(String helpUrl) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_HELP_URL, helpUrl);
        return this;
    }

    public LightAdmin fileStoragePath(String fileStoragePath) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_FILE_STORAGE_PATH, fileStoragePath);
        return this;
    }

    public LightAdmin fileStreaming(boolean fileStreaming) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_FILE_STREAMING, Boolean.toString(fileStreaming));
        return this;
    }

    public LightAdmin demoMode() {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_DEMO_MODE, Boolean.toString(true));
        return this;
    }
}