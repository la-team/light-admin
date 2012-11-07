package org.lightadmin.core.config.domain.support;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class DomainTypeConfigurationUnit implements ConfigurationUnit {

	private Class<?> domainType;

	protected DomainTypeConfigurationUnit( final Class<?> domainType ) {
		this.domainType = domainType;
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	@Override
	public boolean equals( final Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final DomainTypeConfigurationUnit that = ( DomainTypeConfigurationUnit ) o;

		if ( getDomainConfigurationUnitType() != that.getDomainConfigurationUnitType() ) {
			return false;
		}

		return domainType.equals( that.domainType );
	}

	@Override
	public int hashCode() {
		int result = domainType.hashCode();
		result = 31 * result + getDomainConfigurationUnitType().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder( this ).append( "domainType", domainType ).append( "domainConfigurationUnitType", getDomainConfigurationUnitType() ).toString();
	}
}