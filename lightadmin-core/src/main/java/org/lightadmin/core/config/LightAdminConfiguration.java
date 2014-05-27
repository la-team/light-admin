package org.lightadmin.core.config;

import java.io.File;
import java.net.URI;

public interface LightAdminConfiguration {

    String getBasePackage();

    String getApplicationBaseUrl();

    String getApplicationUrl(String path);

    URI getApplicationRestBaseUrl();

    boolean isSecurityEnabled();

    String getSecurityLogoutUrl();

    String getBackToSiteUrl();

    File getFileStorageDirectory();

    boolean isFileStreamingEnabled();

}