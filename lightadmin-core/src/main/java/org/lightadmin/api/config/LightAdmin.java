package org.lightadmin.api.config;

import javax.servlet.ServletContext;

import static org.lightadmin.core.util.LightAdminConfigurationUtils.*;

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

    public LightAdmin fileStoragePath(String fileStoragePath) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_FILE_STORAGE_PATH, fileStoragePath);
        return this;
    }

    public LightAdmin fileStreaming(boolean fileStreaming) {
        servletContext.setInitParameter(LIGHT_ADMINISTRATION_FILE_STREAMING, Boolean.toString(fileStreaming));
        return this;
    }
}