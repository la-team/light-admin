package org.lightadmin.core.context;

import javax.servlet.ServletContext;
import java.io.File;

import static org.apache.commons.io.FileUtils.getFile;
import static org.apache.commons.lang.BooleanUtils.toBoolean;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.lightadmin.core.util.LightAdminConfigurationUtils.*;

public class StandardWebContext implements WebContext {

    private final String applicationBaseUrl;
    private final String applicationBaseNoEndSeparator;
    private final boolean securityEnabled;
    private final String securityLogoutUrl;
    private final String backToSiteUrl;
    private final File fileStorageDirectory;
    private final boolean fileStreaming;

    public StandardWebContext(ServletContext servletContext) {
        this.applicationBaseUrl = servletContext.getInitParameter(LIGHT_ADMINISTRATION_BASE_URL);
        this.applicationBaseNoEndSeparator = urlWithoutEndSeparator(this.applicationBaseUrl);
        this.backToSiteUrl = backToSiteUrl(servletContext);

        this.fileStorageDirectory = fileStorageDirectory(servletContext);
        this.fileStreaming = toBoolean(servletContext.getInitParameter(LIGHT_ADMINISTRATION_FILE_STREAMING));

        this.securityEnabled = toBoolean(servletContext.getInitParameter(LIGHT_ADMINISTRATION_SECURITY));
        if (securityEnabled) {
            this.securityLogoutUrl = servletContext.getContextPath() + this.applicationBaseNoEndSeparator + LIGHT_ADMIN_SECURITY_LOGOUT_URL_DEFAULT;
        } else {
            this.securityLogoutUrl = servletContext.getContextPath() + defaultIfBlank(servletContext.getInitParameter(LIGHT_ADMINISTRATION_SECURITY_LOGOUT_URL), "#");
        }
    }

    @Override
    public File getFileStorageDirectory() {
        return fileStorageDirectory;
    }

    @Override
    public boolean isFileStorageEnabled() {
        return this.fileStorageDirectory != null;
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

    @Override
    public String getSecurityLogoutUrl() {
        return securityLogoutUrl;
    }

    private File fileStorageDirectory(ServletContext servletContext) {
        final String fileStoragePath = servletContext.getInitParameter(LIGHT_ADMINISTRATION_FILE_STORAGE_PATH);
        if (isBlank(fileStoragePath)) {
            return null;
        }
        final File fileStorageDirectory = getFile(fileStoragePath);

        if (fileStorageDirectory.exists() && fileStorageDirectory.canWrite()) {
            return fileStorageDirectory;
        }

        return null;
    }

    private String backToSiteUrl(ServletContext servletContext) {
        final String backToSiteUrl = servletContext.getInitParameter(LIGHT_ADMINISTRATION_BACK_TO_SITE_URL);

        if (isBlank(backToSiteUrl)) {
            return "#";
        }

        if (backToSiteUrl.startsWith("/")) {
            return servletContext.getContextPath() + backToSiteUrl;
        }

        return backToSiteUrl;
    }

    private String urlWithoutEndSeparator(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}