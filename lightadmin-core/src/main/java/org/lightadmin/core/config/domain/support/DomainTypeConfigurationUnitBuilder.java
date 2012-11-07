package org.lightadmin.core.config.domain.support;

public abstract class DomainTypeConfigurationUnitBuilder<U extends ConfigurationUnit> implements ConfigurationUnitBuilder<U> {

	private final Class<?> domainType;

	protected DomainTypeConfigurationUnitBuilder( final Class<?> domainType ) {
		this.domainType = domainType;
	}

	public Class<?> getDomainType() {
		return domainType;
	}
}