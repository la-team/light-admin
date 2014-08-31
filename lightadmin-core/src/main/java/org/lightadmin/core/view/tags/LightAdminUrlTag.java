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
package org.lightadmin.core.view.tags;

import org.lightadmin.core.config.LightAdminConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import static org.lightadmin.core.web.util.WebContextUtils.getWebApplicationContext;

public class LightAdminUrlTag extends org.springframework.web.servlet.tags.UrlTag {

    @Override
    public int doEndTag() throws JspException {
        final String applicationBaseUrl = lightAdminContext().getApplicationBaseUrl();

        if (!"/".equals(applicationBaseUrl)) {
            setContext(contextPath() + applicationBaseUrl);
        }

        return super.doEndTag();
    }

    private String contextPath() {
        return ((HttpServletRequest) pageContext.getRequest()).getContextPath();
    }

    private LightAdminConfiguration lightAdminContext() {
        return getWebApplicationContext(pageContext.getServletContext()).getBean(LightAdminConfiguration.class);
    }
}