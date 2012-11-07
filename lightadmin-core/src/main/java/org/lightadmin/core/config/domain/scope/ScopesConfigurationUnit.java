package org.lightadmin.core.config.domain.scope;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

public interface ScopesConfigurationUnit extends ConfigurationUnit, Iterable<ScopeMetadata> {

	ScopeMetadata getScope( String name );

}