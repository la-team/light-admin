package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.Scopes;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public interface DomainConfigurationSource<T> {

	T getSource();

	Class<?> getDomainType();

	String getConfigurationName();

	DomainTypeEntityMetadata getDomainTypeEntityMetadata();

	Filters getFilters();

	Scopes getScopes();

	Fragment getListViewFragment();

	ScreenContext getScreenContext();

	EntityConfiguration getEntityConfiguration();
}