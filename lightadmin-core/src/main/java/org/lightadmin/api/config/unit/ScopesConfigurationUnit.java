package org.lightadmin.api.config.unit;

import org.lightadmin.core.config.domain.scope.ScopeMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

public interface ScopesConfigurationUnit extends ConfigurationUnit, Iterable<ScopeMetadata> {

    ScopeMetadata getScope(String name);

}