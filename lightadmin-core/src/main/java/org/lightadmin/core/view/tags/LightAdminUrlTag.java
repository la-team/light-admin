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