package org.lightadmin.core.config.domain.scope;

import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface ScopesConfigurationUnitBuilder extends ConfigurationUnitBuilder<ScopesConfigurationUnit> {

	ScopesConfigurationUnitBuilder scope( final String name, final ScopeMetadata scope );

	ScopesConfigurationUnitBuilder scope( final ScopeMetadata scope );

	ScopesConfigurationUnitBuilder defaultScope();
}