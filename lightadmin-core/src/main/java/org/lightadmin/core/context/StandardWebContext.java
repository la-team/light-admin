package org.lightadmin.core.context;

public class StandardWebContext implements WebContext {

	private final String applicationBaseUrl;
	private final String applicationBaseNoEndSeparator;
	private final boolean securityEnabled;

	public StandardWebContext( final String applicationBaseUrl, final boolean securityEnabled ) {
		this.applicationBaseUrl = applicationBaseUrl;
		this.applicationBaseNoEndSeparator = applicationBaseUrl.endsWith("/") ? applicationBaseUrl.substring(0, applicationBaseUrl.length()-1) : applicationBaseUrl;
		this.securityEnabled = securityEnabled;
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
}
