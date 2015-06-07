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
package org.lightadmin.core.util;

public class LightAdminConfigurationUtils {

    public static final String LIGHT_ADMINISTRATION_BASE_PACKAGE = "light:administration:base-package";
    public static final String LIGHT_ADMINISTRATION_BASE_URL = "light:administration:base-url";
    public static final String LIGHT_ADMINISTRATION_SECURITY = "light:administration:security";
    public static final String LIGHT_ADMINISTRATION_SECURITY_LOGOUT_URL = "light:administration:security-logout-url";
    public static final String LIGHT_ADMINISTRATION_BACK_TO_SITE_URL = "light:administration:back-to-site-url";
    public static final String LIGHT_ADMINISTRATION_HELP_URL = "light:administration:help-url";
    public static final String LIGHT_ADMINISTRATION_FILE_STORAGE_PATH = "light:administration:file-storage";
    public static final String LIGHT_ADMINISTRATION_FILE_STREAMING = "light:administration:file-streaming";

    public static final String LIGHT_ADMIN_DISPATCHER_NAME = "lightadmin-dispatcher";
    public static final String LIGHT_ADMIN_CUSTOM_RESOURCE_SERVLET_NAME = "lightadmin-cusom-resource-servlet";
    public static final String LIGHT_ADMIN_LOGO_RESOURCE_SERVLET_NAME = "lightadmin-logo-resource-servlet";
    public static final String LIGHT_ADMIN_DISPATCHER_REDIRECTOR_NAME = "lightadmin-dispatcher-redirector";

    public static final String LIGHT_ADMIN_SECURITY_LOGOUT_URL_DEFAULT = "/logout";

    public static final String LIGHT_ADMIN_REST_URL_DEFAULT = "/rest";

    public static final String LIGHT_ADMIN_CUSTOM_RESOURCE_LOGO = "/images/lightadmin-custom-logo.png";

    public static final String LIGHT_ADMIN_CUSTOM_RESOURCE_WEB_INF_LOCATION = "/WEB-INF/lightadmin";
    public static final String LIGHT_ADMIN_CUSTOM_RESOURCE_LOGO_WEB_INF_LOCATION = LIGHT_ADMIN_CUSTOM_RESOURCE_WEB_INF_LOCATION + LIGHT_ADMIN_CUSTOM_RESOURCE_LOGO;

    public static final String LIGHT_ADMIN_CUSTOM_RESOURCE_FRAGMENT_LOCATION = LIGHT_ADMIN_CUSTOM_RESOURCE_WEB_INF_LOCATION + "/**/*.jsp";

    public static final String LIGHT_ADMIN_CUSTOM_RESOURCE_CLASSPATH_LOCATION = "classpath:/META-INF/resources/lightadmin";
    public static final String LIGHT_ADMIN_CUSTOM_RESOURCE_LOGO_CLASSPATH_LOCATION = LIGHT_ADMIN_CUSTOM_RESOURCE_CLASSPATH_LOCATION + LIGHT_ADMIN_CUSTOM_RESOURCE_LOGO;

    public static final String LIGHT_ADMIN_DEFAULT_LOGO_LOCATION = "/images/lightadmin-logo.png";

    public static final String LIGHT_ADMIN_CUSTOM_FRAGMENT_SERVLET_URL = "/dynamic/custom";
    public static final String LIGHT_ADMIN_LOGO_SERVLET_URL = "/dynamic/logo";

    public static final String LIGHT_ADMINISTRATION_BACK_TO_SITE_DEFAULT_URL = "http://lightadmin.org";
    public static final String LIGHT_ADMINISTRATION_HELP_DEFAULT_URL = "http://lightadmin.org/getting-started/";

    public static final String LIGHT_ADMINISTRATION_DEMO_MODE = "light:administration:demo-mode";
}