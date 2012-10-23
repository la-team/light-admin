package org.lightadmin.core.config.beans.parsing;

import org.lightadmin.core.config.beans.parsing.configuration.DomainConfigurationInterface;
import org.lightadmin.core.config.domain.configuration.EntityConfiguration;
import org.lightadmin.core.config.domain.context.ScreenContext;
import org.lightadmin.core.config.domain.filter.Filters;
import org.lightadmin.core.config.domain.fragment.Fragment;
import org.lightadmin.core.config.domain.scope.Scopes;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public class DomainConfigurationDecorator implements DomainConfigurationInterface {

	private final DomainConfigurationInterface domainConfiguration;

	private final ConfigurationUnitPropertyFilter propertyFilter;

	public DomainConfigurationDecorator( DomainConfigurationInterface domainConfiguration, ConfigurationUnitPropertyFilter propertyFilter ) {
		this.domainConfiguration = domainConfiguration;
		this.propertyFilter = propertyFilter;
	}

	@Override
	public Class<?> getDomainType() {
		return domainConfiguration.getDomainType();
	}

	@Override
	public DomainTypeEntityMetadata getDomainTypeEntityMetadata() {
		return domainConfiguration.getDomainTypeEntityMetadata();
	}

	@Override
	public Class<?> getConfigurationClass() {
		return domainConfiguration.getConfigurationClass();
	}

	@Override
	public Filters getFilters() {
		return domainConfiguration.getFilters().filter( propertyFilter ).apply( domainConfiguration.getDomainTypeEntityMetadata() );
	}

	@Override
	public Scopes getScopes() {
		return domainConfiguration.getScopes();
	}

	@Override
	public Fragment getListViewFragment() {
		return domainConfiguration.getListViewFragment().filter( propertyFilter );
	}

	@Override
	public ScreenContext getScreenContext() {
		return domainConfiguration.getScreenContext();
	}

	@Override
	public EntityConfiguration getConfiguration() {
		return domainConfiguration.getConfiguration();
	}
}