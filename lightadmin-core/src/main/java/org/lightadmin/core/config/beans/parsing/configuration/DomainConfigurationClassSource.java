package org.lightadmin.core.config.beans.parsing.configuration;

import org.apache.commons.lang.builder.ToStringBuilder;
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
import org.lightadmin.core.config.domain.support.Builder;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.util.Assert;

import static org.lightadmin.core.util.DomainConfigurationUtils.initializeConfigurationUnitWithBuilder;

public class DomainConfigurationClassSource implements DomainConfigurationSource<Class> {

	private final Class configurationClass;

	private final DomainTypeEntityMetadata domainTypeEntityMetadata;

	public DomainConfigurationClassSource( final DomainTypeEntityMetadata domainTypeEntityMetadata, final Class<?> configurationClass ) {
		Assert.notNull( domainTypeEntityMetadata );
		Assert.notNull( configurationClass );

		this.configurationClass = configurationClass;
		this.domainTypeEntityMetadata = domainTypeEntityMetadata;
	}

	@Override
	public Class<?> getDomainType() {
		return domainTypeEntityMetadata.getDomainType();
	}

	@Override
	public String getConfigurationName() {
		return configurationClass.getSimpleName();
	}

	@Override
	public DomainTypeEntityMetadata getDomainTypeEntityMetadata() {
		return domainTypeEntityMetadata;
	}

	@Override
	public Class getSource() {
		return configurationClass;
	}

	@Override
	public Filters getFilters() {
		return initializeConfigurationUnit( DomainConfigurationUnit.FILTERS, FilterBuilder.class, DefaultFilterBuilder.class );
	}

	@Override
	public Scopes getScopes() {
		return initializeConfigurationUnit( DomainConfigurationUnit.SCOPES, ScopeBuilder.class, DefaultScopeBuilder.class );
	}

	@Override
	public Fragment getListViewFragment() {
		return initializeConfigurationUnit( DomainConfigurationUnit.LIST_VIEW, FragmentBuilder.class, TableFragmentBuilder.class );
	}

	@Override
	public ScreenContext getScreenContext() {
		return initializeConfigurationUnit( DomainConfigurationUnit.SCREEN_CONTEXT, ScreenContextBuilder.class, DefaultScreenContextBuilder.class );
	}

	@Override
	public EntityConfiguration getConfiguration() {
		return initializeConfigurationUnit( DomainConfigurationUnit.CONFIGURATION, EntityConfigurationBuilder.class, DefaultEntityConfigurationBuilder.class );
	}

	private <T> T initializeConfigurationUnit( DomainConfigurationUnit configurationUnit, Class<? extends Builder<T>> builderInterface, Class<? extends Builder<T>> concreteBuilderClass ) {
		return initializeConfigurationUnitWithBuilder( configurationClass, configurationUnit, builderInterface, concreteBuilderClass, domainTypeEntityMetadata );
	}

	@Override
	public boolean equals( final Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		final DomainConfigurationClassSource that = ( DomainConfigurationClassSource ) o;

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