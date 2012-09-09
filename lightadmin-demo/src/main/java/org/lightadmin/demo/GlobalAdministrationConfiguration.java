package org.lightadmin.demo;

import java.util.Map;

public class GlobalAdministrationConfiguration {

	private Map<Class<?>, DomainTypeAdministrationConfiguration> domainTypeConfigurations;

	public void setDomainTypeConfigurations( final Map<Class<?>, DomainTypeAdministrationConfiguration> domainTypeConfigurations ) {
		this.domainTypeConfigurations = domainTypeConfigurations;
	}

	public Map<Class<?>, DomainTypeAdministrationConfiguration> getDomainTypeConfigurations() {
		return domainTypeConfigurations;
	}
}