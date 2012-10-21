package org.lightadmin.core.config.beans.parsing;

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
import org.lightadmin.core.reporting.ProblemReporter;

import static org.lightadmin.core.util.ConfigurationClassUtils.initializeConfigurationUnitWithBuilder;
import static org.lightadmin.core.util.ConfigurationClassUtils.isNotConfigurationUnitDefined;

public class DslConfigurationClass {

	private final Class<?> configurationClass;

	private final Class<?> domainType;

	public DslConfigurationClass( final Class<?> domainType, final Class<?> configurationClass ) {
		this.configurationClass = configurationClass;
		this.domainType = domainType;
	}

	public Class<?> getDomainType() {
		return domainType;
	}

	public Class<?> getConfigurationClass() {
		return configurationClass;
	}

	public void validate( ProblemReporter problemReporter ) {
		if ( domainType == null ) {
			problemReporter.fatal( new DslConfigurationProblem( this, "You need to define Domain Type for configuration!" ) );
		}

		if ( isNotConfigurationUnitDefined( configurationClass, "screenContext", ScreenContextBuilder.class ) ) {
			problemReporter.error( new DslConfigurationProblem( this, "You need to define Screen Context configuration unit!" ) );
		}

		if ( isNotConfigurationUnitDefined( configurationClass, "configuration", EntityConfigurationBuilder.class ) ) {
			problemReporter.warning( new DslConfigurationProblem( this, "It's better to define Entity configuration unit or system will use DomainTypeClass#getSimpleName for entity string representation!" ) );
		}
	}

	public Filters getFilters() {
		return initializeConfigurationUnitWithBuilder( configurationClass, "filters", FilterBuilder.class, DefaultFilterBuilder.class );
	}

	public Scopes getScopes() {
		return initializeConfigurationUnitWithBuilder( configurationClass, "scopes", ScopeBuilder.class, DefaultScopeBuilder.class );
	}

	public Fragment getListViewFragment() {
		return initializeConfigurationUnitWithBuilder( configurationClass, "listView", FragmentBuilder.class, TableFragmentBuilder.class );
	}

	public ScreenContext getScreenContext() {
		return initializeConfigurationUnitWithBuilder( configurationClass, "screenContext", ScreenContextBuilder.class, DefaultScreenContextBuilder.class );
	}

	public EntityConfiguration getConfiguration() {
		return initializeConfigurationUnitWithBuilder( configurationClass, "configuration", EntityConfigurationBuilder.class, DefaultEntityConfigurationBuilder.class );
	}
}