package org.lightadmin.core.config;

import java.io.File;

public interface LightAdminConfiguration {

    String getBasePackage();

    String getApplicationBaseUrl();

    String getApplicationUrl(String path);

    String getApplicationRestBaseUrl();

    boolean isSecurityEnabled();

    String getSecurityLogoutUrl();

    String getBackToSiteUrl();

    File getFileStorageDirectory();

    boolean isFileStreamingEnabled();

}