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
import org.springframework.util.Assert;

import java.io.Serializable;

import static org.lightadmin.core.util.DomainConfigurationUtils.configurationDomainType;
import static org.lightadmin.core.util.DomainConfigurationUtils.initializeConfigurationUnitWithBuilder;

public class DomainConfigurationClassDTO implements Serializable {

	private final Class<?> domainType;
	private final Filters filters;
	private final Scopes scopes;
	private final Fragment listViewFragment;
	private final ScreenContext screenContext;
	private final EntityConfiguration entityConfiguration;

	public DomainConfigurationClassDTO( Class configurationClass ) {
		domainType = configurationDomainType( configurationClass );

		Assert.notNull( domainType );

		filters = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnitType.FILTERS, FilterBuilder.class, DefaultFilterBuilder.class );

		scopes = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnitType.SCOPES, ScopeBuilder.class, DefaultScopeBuilder.class );

		listViewFragment = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnitType.LIST_VIEW, FragmentBuilder.class, TableFragmentBuilder.class );

		screenContext = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnitType.SCREEN_CONTEXT, ScreenContextBuilder.class, DefaultScreenContextBuilder.class );

		entityConfiguration = initializeConfigurationUnitWithBuilder( configurationClass, DomainConfigurationUnitType.CONFIGURATION, EntityConfigurationBuilder.class, DefaultEntityConfigurationBuilder.class );
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

	@Override
	public boolean equals( final Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final DomainConfigurationClassDTO that = ( DomainConfigurationClassDTO ) o;

		return domainType.equals( that.domainType );
	}

	@Override
	public int hashCode() {
		return domainType.hashCode();
	}
}