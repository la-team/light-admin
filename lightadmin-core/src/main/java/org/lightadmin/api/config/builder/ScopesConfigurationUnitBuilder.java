package org.lightadmin.api.config.builder;

import org.lightadmin.api.config.unit.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface ScopesConfigurationUnitBuilder extends ConfigurationUnitBuilder<ScopesConfigurationUnit> {

    ScopesConfigurationUnitBuilder scope(final String name, final ScopeMetadata scope);

    ScopesConfigurationUnitBuilder scope(final ScopeMetadata scope);

    ScopesConfigurationUnitBuilder defaultScope();
}