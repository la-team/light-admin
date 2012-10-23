package org.lightadmin.core.config.beans.parsing.configuration;

import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.Scopes;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public interface DomainConfigurationInterface {

	Class<?> getDomainType();

	DomainTypeEntityMetadata getDomainTypeEntityMetadata();

	Class<?> getConfigurationClass();

	Filters getFilters();

	Scopes getScopes();

	Fragment getListViewFragment();

	ScreenContext getScreenContext();

	EntityConfiguration getConfiguration();
}