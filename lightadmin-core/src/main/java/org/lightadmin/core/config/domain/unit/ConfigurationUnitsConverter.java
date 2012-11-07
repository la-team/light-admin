package org.lightadmin.core.config.domain.unit;

import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.DefaultScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.DefaultFiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.ListViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.fragment.TableListViewConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.scope.DefaultScopesConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnitBuilder;
import org.springframework.core.convert.converter.Converter;

import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.*;
import static org.lightadmin.core.util.DomainConfigurationUtils.configurationDomainType;
import static org.lightadmin.core.util.DomainConfigurationUtils.initializeConfigurationUnitWithBuilder;

public final class ConfigurationUnitsConverter implements Converter<Class, ConfigurationUnits> {

	private static final ConfigurationUnitsConverter INSTANCE = new ConfigurationUnitsConverter();

	public static ConfigurationUnits fromConfiguration( Class configurationClass ) {
		return INSTANCE.convert( configurationClass );
	}

	@Override
	public ConfigurationUnits convert( final Class configurationClass ) {
		final Class<?> domainType = configurationDomainType( configurationClass );

		return new ConfigurationUnits( domainType, initializeConfigurationUnitWithBuilder( configurationClass, FILTERS, FiltersConfigurationUnitBuilder.class, DefaultFiltersConfigurationUnitBuilder.class, domainType ), initializeConfigurationUnitWithBuilder( configurationClass, SCOPES, ScopesConfigurationUnitBuilder.class, DefaultScopesConfigurationUnitBuilder.class, domainType ), initializeConfigurationUnitWithBuilder( configurationClass, LIST_VIEW, ListViewConfigurationUnitBuilder.class, TableListViewConfigurationUnitBuilder.class, domainType ), initializeConfigurationUnitWithBuilder( configurationClass, SCREEN_CONTEXT, ScreenContextConfigurationUnitBuilder.class, DefaultScreenContextConfigurationUnitBuilder.class, domainType ), initializeConfigurationUnitWithBuilder( configurationClass, CONFIGURATION, EntityMetadataConfigurationUnitBuilder.class, DefaultEntityMetadataConfigurationUnitBuilder.class, domainType ) );
	}
}