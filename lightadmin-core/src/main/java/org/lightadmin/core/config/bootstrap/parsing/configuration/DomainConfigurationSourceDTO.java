package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.config.domain.configuration.DefaultEntityConfigurationBuilder;
import org.lightadmin.core.config.domain.configuration.EntityConfigurationBuilder;
import org.lightadmin.core.config.domain.context.DefaultScreenContextBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextBuilder;
import org.lightadmin.core.config.domain.filter.DefaultFilterBuilder;
import org.lightadmin.core.config.domain.filter.FilterBuilder;
import org.lightadmin.core.config.domain.fragment.FragmentBuilder;
import org.lightadmin.core.config.domain.fragment.TableFragmentBuilder;
import org.lightadmin.core.config.domain.scope.DefaultScopeBuilder;
import org.lightadmin.core.config.domain.scope.ScopeBuilder;
import org.lightadmin.core.config.domain.support.Builder;

import static org.lightadmin.core.util.DomainConfigurationUtils.configurationDomainType;

public class DomainConfigurationSourceDTO {

	public DomainConfigurationSourceDTO( Class configurationClass ) {
		configurationDomainType(configurationClass);
		initializeConfigurationUnit( DomainConfigurationUnit.FILTERS, FilterBuilder.class, DefaultFilterBuilder.class );
		initializeConfigurationUnit( DomainConfigurationUnit.SCOPES, ScopeBuilder.class, DefaultScopeBuilder.class );
		initializeConfigurationUnit( DomainConfigurationUnit.LIST_VIEW, FragmentBuilder.class, TableFragmentBuilder.class );
		initializeConfigurationUnit( DomainConfigurationUnit.SCREEN_CONTEXT, ScreenContextBuilder.class, DefaultScreenContextBuilder.class );
		initializeConfigurationUnit( DomainConfigurationUnit.CONFIGURATION, EntityConfigurationBuilder.class, DefaultEntityConfigurationBuilder.class );
	}


	private <T> T initializeConfigurationUnit( DomainConfigurationUnit configurationUnit, Class<? extends Builder<T>> builderInterface, Class<? extends Builder<T>> concreteBuilderClass ) {
		return null;
//		return initializeConfigurationUnitWithBuilder( configurationClass, configurationUnit, builderInterface, concreteBuilderClass, domainTypeEntityMetadata );
	}
}