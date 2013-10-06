package org.lightadmin.core.context;

import java.io.File;

public interface WebContext {

    String getApplicationBaseUrl();

    String getApplicationUrl(String path);

    boolean isSecurityEnabled();

    String getSecurityLogoutUrl();

    String getBackToSiteUrl();

    File getFileStorageDirectory();

    boolean isFileStreamingEnabled();

}