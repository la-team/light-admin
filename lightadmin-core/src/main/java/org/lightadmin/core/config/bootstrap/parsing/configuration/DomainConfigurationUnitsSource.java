package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.config.domain.configuration.EntityMetadataConfigurationUnit;
import org.lightadmin.core.config.domain.context.ScreenContextConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.scope.ScopesConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.util.Assert;

public class DomainConfigurationUnitsSource implements DomainConfigurationSource {

	private final ConfigurationUnits configurationUnits;

	private final DomainTypeEntityMetadata domainTypeEntityMetadata;

	public DomainConfigurationUnitsSource( final DomainTypeEntityMetadata domainTypeEntityMetadata, ConfigurationUnits configurationUnits ) {
		Assert.notNull( domainTypeEntityMetadata, "DomainTypeEntityMetadata must not be null" );
		Assert.notNull( configurationUnits, "ConfigurationUnits must not be null" );

		this.configurationUnits = configurationUnits;
		this.domainTypeEntityMetadata = domainTypeEntityMetadata;
	}

	@Override
	public Class<?> getDomainType() {
		return configurationUnits.getDomainType();
	}

	@Override
	public String getConfigurationName() {
		return String.format( "%sConfiguration", getDomainType().getSimpleName() );
	}

	@Override
	public DomainTypeEntityMetadata getDomainTypeEntityMetadata() {
		return domainTypeEntityMetadata;
	}

	@Override
	public FiltersConfigurationUnit getFilters() {
		return configurationUnits.getFilters();
	}

	@Override
	public ScopesConfigurationUnit getScopes() {
		return configurationUnits.getScopes();
	}

	@Override
	public FieldSetConfigurationUnit getQuickViewFragment() {
		return configurationUnits.getQuickViewConfigurationUnit();
	}

	@Override
	public FieldSetConfigurationUnit getListViewFragment() {
		return configurationUnits.getListViewConfigurationUnit();
	}

	@Override
	public FieldSetConfigurationUnit getShowViewFragment() {
		return configurationUnits.getShowViewConfigurationUnit();
	}

	@Override
	public FieldSetConfigurationUnit getFormViewFragment() {
		return configurationUnits.getFormViewConfigurationUnit();
	}

	@Override
	public ScreenContextConfigurationUnit getScreenContext() {
		return configurationUnits.getScreenContext();
	}

	@Override
	public EntityMetadataConfigurationUnit getEntityConfiguration() {
		return configurationUnits.getEntityConfiguration();
	}
}