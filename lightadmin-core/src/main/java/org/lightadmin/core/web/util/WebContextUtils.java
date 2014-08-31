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
package org.lightadmin.core.web.util;

import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.util.LightAdminConfigurationUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public abstract class WebContextUtils {

    public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
        return WebApplicationContextUtils.getWebApplicationContext(servletContext, servletContextAttributeName());
    }

    public static String servletContextAttributeName() {
        return FrameworkServlet.SERVLET_CONTEXT_PREFIX + LightAdminConfigurationUtils.LIGHT_ADMIN_DISPATCHER_NAME;
    }

    public static GlobalAdministrationConfiguration globalAdministrationConfiguration(ServletContext servletContext) {
        WebApplicationContext webApplicationContext = getWebApplicationContext(servletContext);
        return webApplicationContext.getBean(GlobalAdministrationConfiguration.class);
    }

    public static FileResourceLoader fileResourceLoader(ServletContext servletContext) {
        WebApplicationContext webApplicationContext = getWebApplicationContext(servletContext);
        return webApplicationContext.getBean(FileResourceLoader.class);
    }

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }
}