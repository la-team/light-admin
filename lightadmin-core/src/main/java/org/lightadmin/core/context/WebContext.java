package org.lightadmin.core.context;

public interface WebContext {

    String getApplicationBaseUrl();

    String getApplicationUrl(String path);

    boolean isSecurityEnabled();

    String getSecurityLogoutUrl();
}
