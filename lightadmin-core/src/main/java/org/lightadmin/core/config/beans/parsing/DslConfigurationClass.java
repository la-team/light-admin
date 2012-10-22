package org.lightadmin.core.config.beans.parsing;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lightadmin.core.config.domain.configuration.DefaultEntityConfigurationBuilder;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.config.domain.context.DefaultScreenContextBuilder;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.context.ScreenContextBuilder;
import org.lightadmin.core.config.domain.filter.DefaultFilterBuilder;
import org.lightadmin.core.config.domain.filter.Filter;
import org.lightadmin.core.config.domain.filter.FilterBuilder;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.core.config.domain.fragment.TableFragment;
import org.lightadmin.core.config.domain.fragment.TableFragmentBuilder;
import org.lightadmin.core.config.domain.scope.DefaultScopeBuilder;
import org.lightadmin.core.config.domain.scope.ScopeBuilder;
import org.lightadmin.core.config.domain.scope.Scopes;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.reporting.ProblemReporter;
import org.lightadmin.core.util.Pair;
import org.springframework.util.Assert;

import java.util.Set;

import static org.lightadmin.core.util.ConfigurationClassUtils.*;

public class DslConfigurationClass {

	private final Class<?> configurationClass;

	private final DomainTypeEntityMetadata domainTypeEntityMetadata;

	public DslConfigurationClass( final DomainTypeEntityMetadata domainTypeEntityMetadata, final Class<?> configurationClass ) {
		Assert.notNull( domainTypeEntityMetadata );
		Assert.notNull( configurationClass );

		this.configurationClass = configurationClass;
		this.domainTypeEntityMetadata = domainTypeEntityMetadata;
	}

	public Class<?> getDomainType() {
		return domainTypeEntityMetadata.getDomainType();
	}

	public DomainTypeEntityMetadata getDomainTypeEntityMetadata() {
		return domainTypeEntityMetadata;
	}

	public Class<?> getConfigurationClass() {
		return configurationClass;
	}

	public Filters getFilters() {
		final Filters filters = initializeConfigurationUnitWithBuilder( configurationClass, DslConfigurationUnit.FILTERS, FilterBuilder.class, DefaultFilterBuilder.class );

		for ( Filter filter : filters ) {
			filter.setAttributeMetadata( domainTypeEntityMetadata.getAttribute( filter.getFieldName() ) );
		}

		return filters;
	}

	public Scopes getScopes() {
		return initializeConfigurationUnitWithBuilder( configurationClass, DslConfigurationUnit.SCOPES, ScopeBuilder.class, DefaultScopeBuilder.class );
	}

	public Fragment getListViewFragment() {
		return initializeConfigurationUnitWithBuilder( configurationClass, DslConfigurationUnit.LIST_VIEW, FragmentBuilder.class, TableFragmentBuilder.class );
	}

	public ScreenContext getScreenContext() {
		return initializeConfigurationUnitWithBuilder( configurationClass, DslConfigurationUnit.SCREEN_CONTEXT, ScreenContextBuilder.class, DefaultScreenContextBuilder.class );
	}

	public EntityConfiguration getConfiguration() {
		return initializeConfigurationUnitWithBuilder( configurationClass, DslConfigurationUnit.CONFIGURATION, EntityConfigurationBuilder.class, DefaultEntityConfigurationBuilder.class );
	}

	public void validate( ProblemReporter problemReporter ) {
		if ( isNotConfigurationUnitDefined( configurationClass, DslConfigurationUnit.SCREEN_CONTEXT, ScreenContextBuilder.class ) ) {
			problemReporter.error( new DslConfigurationProblem( this, "You need to define screenContext configuration unit!" ) );
		}

		if ( isNotConfigurationUnitDefined( configurationClass, DslConfigurationUnit.CONFIGURATION, EntityConfigurationBuilder.class ) ) {
			problemReporter.warning( new DslConfigurationProblem( this, "It's better to define Entity configuration unit or system will use DomainTypeClass#getSimpleName for entity string representation!" ) );
		}

		if ( isConfigurationUnitDefined( configurationClass, DslConfigurationUnit.FILTERS, FilterBuilder.class ) ) {
			validateFilters( problemReporter );
		}

		if ( isConfigurationUnitDefined( configurationClass, DslConfigurationUnit.LIST_VIEW, FragmentBuilder.class ) ) {
			validateListView( problemReporter );
		}
	}

	private void validateListView( final ProblemReporter problemReporter ) {
		final TableFragment listViewFragment = ( TableFragment ) getListViewFragment();
		final Set<Pair<String, String>> columnsPairs = listViewFragment.getColumns();

		for ( Pair<String, String> columnsPair : columnsPairs ) {
			validateEntityFieldName( columnsPair.first, DslConfigurationUnit.LIST_VIEW, problemReporter );
		}
	}

	private void validateFilters( final ProblemReporter problemReporter ) {
		for ( Filter filter : getFilters() ) {
			validateEntityFieldName( filter.getFieldName(), DslConfigurationUnit.FILTERS, problemReporter );
		}
	}

	private void validateEntityFieldName( final String fieldName, DslConfigurationUnit configurationUnit, final ProblemReporter problemReporter ) {
		final DomainTypeAttributeMetadata attributeMetadata = domainTypeEntityMetadata.getAttribute( fieldName );
		if ( attributeMetadata == null ) {
			problemReporter.warning( new DslConfigurationProblem( this, configurationUnit, String.format( "Unexisting property '%s' defined!", fieldName ) ) );
		}
	}

	@Override
	public boolean equals( final Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final DslConfigurationClass that = ( DslConfigurationClass ) o;

		return configurationClass.equals( that.configurationClass ) && getDomainType().equals( that.getDomainType() );
	}

	@Override
	public int hashCode() {
		int result = configurationClass.hashCode();
		result = 31 * result + getDomainType().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder( this ).append( "domainType", getDomainType() ).append( "configurationClass", configurationClass ).toString();
	}
}