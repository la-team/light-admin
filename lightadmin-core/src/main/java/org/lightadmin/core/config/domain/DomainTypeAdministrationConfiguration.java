package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.*;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.support.Repositories;

import java.io.Serializable;

import static org.springframework.util.StringUtils.uncapitalize;

public class DomainTypeAdministrationConfiguration implements DomainTypeBasicConfiguration {

    private final DynamicJpaRepository<?, ? extends Serializable> repository;
    private final PersistentEntity persistentEntity;
    private final ConfigurationUnits configurationUnits;

    public DomainTypeAdministrationConfiguration(Repositories repositories, ConfigurationUnits configurationUnits) {
        final Class<?> domainType = configurationUnits.getDomainType();

        this.repository = (DynamicJpaRepository<?, ? extends Serializable>) repositories.getRepositoryFor(domainType);
        this.persistentEntity = repositories.getPersistentEntity(domainType);
        this.configurationUnits = configurationUnits;
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
