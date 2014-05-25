package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.api.config.unit.*;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.util.Assert;

public class DomainConfigurationUnitsSource implements DomainConfigurationSource {

    private final ConfigurationUnits configurationUnits;

    private final PersistentEntity persistentEntity;

    public DomainConfigurationUnitsSource(final PersistentEntity persistentEntity, ConfigurationUnits configurationUnits) {
        Assert.notNull(persistentEntity, "DomainTypeEntityMetadata must not be null");
        Assert.notNull(configurationUnits, "ConfigurationUnits must not be null");

        this.configurationUnits = configurationUnits;
        this.persistentEntity = persistentEntity;
    }

    @Override
    public Class<?> getDomainType() {
        return configurationUnits.getDomainType();
    }

    @Override
    public String getConfigurationName() {
        return configurationUnits.getConfigurationClassName();
    }

    @Override
    public PersistentEntity getPersistentEntity() {
        return persistentEntity;
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

    @Override
    public SidebarsConfigurationUnit getSidebars() {
        return configurationUnits.getSidebars();
    }
}