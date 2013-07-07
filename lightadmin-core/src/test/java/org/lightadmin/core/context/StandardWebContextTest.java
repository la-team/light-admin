package org.lightadmin.core.context;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContext;

import static java.lang.String.valueOf;
import static org.junit.Assert.*;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.*;

public class StandardWebContextTest {

    @Test
    public void testApplicationBaseUrlWithoutEndSeparator() {
        WebContext webContext = new StandardWebContext(servletContext("/admin"));

        assertEquals("/admin", webContext.getApplicationBaseUrl());
        assertEquals("/admin/url", webContext.getApplicationUrl("/url"));
    }

    @Test
    public void testApplicationBaseUrlWithEndSeparator() {
        WebContext webContext = new StandardWebContext(servletContext("/admin/"));

        assertEquals("/admin/", webContext.getApplicationBaseUrl());
        assertEquals("/admin/url", webContext.getApplicationUrl("/url"));
    }

    @Test
    public void testSecurityEnabled() {
        final ServletContext servletContext = servletContext("/admin/", true, null);

        WebContext webContext = new StandardWebContext(servletContext);

        assertTrue(webContext.isSecurityEnabled());
        assertEquals("/lightadmin-demo/admin/logout", webContext.getSecurityLogoutUrl());
    }

    @Test
    public void testSecurityDisabledByDefault() {
        final ServletContext servletContext = servletContext("/admin/", null, null);

        WebContext webContext = new StandardWebContext(servletContext);

        assertFalse(webContext.isSecurityEnabled());
        assertEquals("/lightadmin-demo#", webContext.getSecurityLogoutUrl());
    }

    @Test
    public void testExternalSecurityEnabledWithLogoutUrl() {
        final ServletContext servletContext = servletContext("/admin/", false, "/external_logout");

        WebContext webContext = new StandardWebContext(servletContext);

        assertFalse(webContext.isSecurityEnabled());
        assertEquals("/lightadmin-demo/external_logout", webContext.getSecurityLogoutUrl());
    }

    private ServletContext servletContext(final String applicationBaseUrl) {
        return servletContext(applicationBaseUrl, false, null);
    }

    private ServletContext servletContext(final String applicationBaseUrl, final Boolean securityEnabled, String securityLogoutUrl) {
        final MockServletContext servletContext = new MockServletContext();
        servletContext.setContextPath("/lightadmin-demo");
        servletContext.addInitParameter(LIGHT_ADMINISTRATION_BASE_URL, applicationBaseUrl);
        servletContext.addInitParameter(LIGHT_ADMINISTRATION_SECURITY, valueOf(securityEnabled));
        servletContext.addInitParameter(LIGHT_ADMINISTRATION_SECURITY_LOGOUT_URL, securityLogoutUrl);
        return servletContext;
    }
}