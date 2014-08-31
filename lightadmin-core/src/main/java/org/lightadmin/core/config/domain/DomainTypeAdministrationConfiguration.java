/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.*;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.support.Repositories;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Set;

import static org.springframework.util.StringUtils.uncapitalize;

public class DomainTypeAdministrationConfiguration implements DomainTypeBasicConfiguration {

    private static RelProvider REL_PROVIDER = new EvoInflectorRelProvider();

    private final DynamicJpaRepository<?, ? extends Serializable> repository;
    private final PersistentEntity persistentEntity;
    private final ConfigurationUnits configurationUnits;

    public DomainTypeAdministrationConfiguration(Repositories repositories, ConfigurationUnits configurationUnits) {
        Assert.notNull(repositories, "Repositories must not be null!");
        Assert.notNull(configurationUnits, "ConfigurationUnits must not be null!");

        Class<?> domainType = configurationUnits.getDomainType();

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
        return uncapitalize(getDomainType().getSimpleName());
    }

    @Override
    public String getPluralDomainTypeName() {
        return REL_PROVIDER.getCollectionResourceRelFor(getDomainType());
    }

    public Set<FieldMetadata> fieldsForUnit(DomainConfigurationUnitType configurationUnitType) {
        switch (configurationUnitType) {
            case LIST_VIEW:
                return getListViewFragment().getFields();
            case SHOW_VIEW:
                return getShowViewFragment().getFields();
            case FORM_VIEW:
                return getFormViewFragment().getFields();
            case QUICK_VIEW:
                return getQuickViewFragment().getFields();
            default:
                return getShowViewFragment().getFields();
        }
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
