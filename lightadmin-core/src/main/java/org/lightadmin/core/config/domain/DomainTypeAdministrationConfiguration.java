package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.*;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.mapping.PersistentEntity;

import java.io.Serializable;

import static org.springframework.util.StringUtils.uncapitalize;

public class DomainTypeAdministrationConfiguration implements DomainTypeBasicConfiguration {

    private final DynamicJpaRepository<?, ? extends Serializable> repository;

    private PersistentEntity persistentEntity;

    private final ConfigurationUnits configurationUnits;

    public DomainTypeAdministrationConfiguration(final DynamicJpaRepository<?, ?> repository, PersistentEntity persistentEntity, ConfigurationUnits configurationUnits) {
        this.persistentEntity = persistentEntity;
        this.configurationUnits = configurationUnits;
        this.repository = repository;
    }

    @Override
    public PersistentEntity getPersistentEntity() {
        return persistentEntity;
    }

    @Override
    public Class<?> getDomainType() {
        return persistentEntity.getType();
    }

    @Override
    public DynamicJpaRepository getRepository() {
        return repository;
    }

    @Override
    public String getDomainTypeName() {
        return uncapitalize(persistentEntity.getType().getSimpleName());
    }

    public FieldSetConfigurationUnit getQuickViewFragment() {
        return configurationUnits.getQuickViewConfigurationUnit();
    }

    public FieldSetConfigurationUnit getListViewFragment() {
        return configurationUnits.getListViewConfigurationUnit();
    }

    public FieldSetConfigurationUnit getShowViewFragment() {
        return configurationUnits.getShowViewConfigurationUnit();
    }

    public FieldSetConfigurationUnit getFormViewFragment() {
        return configurationUnits.getFormViewConfigurationUnit();
    }

    public ScreenContextConfigurationUnit getScreenContext() {
        return configurationUnits.getScreenContext();
    }

    public ScopesConfigurationUnit getScopes() {
        return configurationUnits.getScopes();
    }

    public SidebarsConfigurationUnit getSidebars() {
        return configurationUnits.getSidebars();
    }

    public FiltersConfigurationUnit getFilters() {
        return configurationUnits.getFilters();
    }

    @Override
    public EntityMetadataConfigurationUnit getEntityConfiguration() {
        return configurationUnits.getEntityConfiguration();
    }

    @Override
    public String getConfigurationName() {
        return configurationUnits.getConfigurationClassName();
    }
}
