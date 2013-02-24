package org.lightadmin.core.context;

public class StandardWebContext implements WebContext {

	private final String applicationBaseUrl;
	private final boolean securityEnabled;

	public StandardWebContext( final String applicationBaseUrl, final boolean securityEnabled ) {
		this.applicationBaseUrl = applicationBaseUrl;
		this.securityEnabled = securityEnabled;
	}

	@Override
	public String getApplicationBaseUrl() {
		return applicationBaseUrl;
	}

	@Override
	public boolean isSecurityEnabled() {
		return securityEnabled;
	}
}