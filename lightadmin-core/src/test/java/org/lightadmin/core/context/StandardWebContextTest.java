package org.lightadmin.core.context;

import org.junit.Test;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.StandardLightAdminConfiguration;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContext;

import static java.lang.String.valueOf;
import static org.junit.Assert.*;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.*;

public class StandardWebContextTest {

    @Test
    public void testApplicationBaseUrlWithoutEndSeparator() {
        LightAdminConfiguration lightAdminConfiguration = new StandardLightAdminConfiguration(servletContext("/admin"));

        assertEquals("/admin", lightAdminConfiguration.getApplicationBaseUrl());
        assertEquals("/admin/url", lightAdminConfiguration.getApplicationUrl("/url"));
    }

    @Test
    public void testApplicationBaseUrlWithEndSeparator() {
        LightAdminConfiguration lightAdminConfiguration = new StandardLightAdminConfiguration(servletContext("/admin/"));

        assertEquals("/admin/", lightAdminConfiguration.getApplicationBaseUrl());
        assertEquals("/admin/url", lightAdminConfiguration.getApplicationUrl("/url"));
    }

    @Test
    public void testSecurityEnabled() {
        final ServletContext servletContext = servletContext("/admin/", true, null);

        LightAdminConfiguration lightAdminConfiguration = new StandardLightAdminConfiguration(servletContext);

        assertTrue(lightAdminConfiguration.isSecurityEnabled());
        assertEquals("/lightadmin-demo/admin/logout", lightAdminConfiguration.getSecurityLogoutUrl());
    }

    @Test
    public void testSecurityDisabledByDefault() {
        final ServletContext servletContext = servletContext("/admin/", null, null);

        LightAdminConfiguration lightAdminConfiguration = new StandardLightAdminConfiguration(servletContext);

        assertFalse(lightAdminConfiguration.isSecurityEnabled());
        assertEquals("/lightadmin-demo#", lightAdminConfiguration.getSecurityLogoutUrl());
    }

    @Test
    public void testExternalSecurityEnabledWithLogoutUrl() {
        final ServletContext servletContext = servletContext("/admin/", false, "/external_logout");

        LightAdminConfiguration lightAdminConfiguration = new StandardLightAdminConfiguration(servletContext);

        assertFalse(lightAdminConfiguration.isSecurityEnabled());
        assertEquals("/lightadmin-demo/external_logout", lightAdminConfiguration.getSecurityLogoutUrl());
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