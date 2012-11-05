package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.config.domain.configuration.DefaultEntityConfigurationBuilder;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.config.domain.context.DefaultScreenContextBuilder;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.context.ScreenContextBuilder;
import org.lightadmin.core.config.domain.filter.DefaultFilterBuilder;
import org.lightadmin.core.config.domain.filter.FilterBuilder;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.core.config.domain.fragment.TableFragmentBuilder;
import org.lightadmin.core.config.domain.scope.DefaultScopeBuilder;
import org.lightadmin.core.config.domain.scope.ScopeBuilder;
import org.lightadmin.core.config.domain.scope.Scopes;

import static org.lightadmin.core.util.DomainConfigurationUtils.configurationDomainType;
import static org.lightadmin.core.util.DomainConfigurationUtils.initializeConfigurationUnitWithBuilder;

public class DomainConfigurationClassDTO {

	private final Class<?> domainType;
	private final Filters filters;
	private final Scopes scopes;
	private final Fragment listViewFragment;
	private final ScreenContext screenContext;
	private final EntityConfiguration entityConfiguration;

	public DomainConfigurationClassDTO( Class configurationClass ) {
		domainType = configurationDomainType( configurationClass );

		filters = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnit.FILTERS, FilterBuilder.class, DefaultFilterBuilder.class );

		scopes = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnit.SCOPES, ScopeBuilder.class, DefaultScopeBuilder.class );

		listViewFragment = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnit.LIST_VIEW, FragmentBuilder.class, TableFragmentBuilder.class );

		screenContext = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnit.SCREEN_CONTEXT, ScreenContextBuilder.class, DefaultScreenContextBuilder.class );

		entityConfiguration = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnit.CONFIGURATION, EntityConfigurationBuilder.class, DefaultEntityConfigurationBuilder.class );
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	public Filters getFilters() {
		return filters;
	}

	public Scopes getScopes() {
		return scopes;
	}

	public Fragment getListViewFragment() {
		return listViewFragment;
	}

	public ScreenContext getScreenContext() {
		return screenContext;
	}

	public EntityConfiguration getEntityConfiguration() {
		return entityConfiguration;
	}
}