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

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.util.Assert;

import java.io.Serializable;

import static org.springframework.util.StringUtils.uncapitalize;

public class NonManagedDomainTypeConfiguration implements DomainTypeBasicConfiguration {

    private static RelProvider REL_PROVIDER = new EvoInflectorRelProvider();

    private final EntityMetadataConfigurationUnit entityConfiguration;

    private final JpaRepository<?, ? extends Serializable> repository;
    private final PersistentEntity persistentEntity;

    public NonManagedDomainTypeConfiguration(EntityMetadataConfigurationUnit entityConfiguration, PersistentEntity persistentEntity, JpaRepository<?, ? extends Serializable> repository) {
        Assert.notNull(persistentEntity, "Persistent Entity must not be null!");
        Assert.notNull(repository, "Repository must not be null!");
        Assert.notNull(repository, "Entity Configuration must not be null!");

        this.entityConfiguration = entityConfiguration;
        this.persistentEntity = persistentEntity;
        this.repository = repository;
    }

    @Override
    public String getConfigurationName() {
        return persistentEntity.getType().getSimpleName();
    }

    @Override
    public Class<?> getDomainType() {
        return persistentEntity.getType();
    }

    @Override
    public String getDomainTypeName() {
        return uncapitalize(getDomainType().getSimpleName());
    }

    @Override
    public String getPluralDomainTypeName() {
        return REL_PROVIDER.getCollectionResourceRelFor(getDomainType());
    }

    @Override
    public PersistentEntity getPersistentEntity() {
        return persistentEntity;
    }

    @Override
    public JpaRepository<?, ?> getRepository() {
        return repository;
    }

    @Override
    public EntityMetadataConfigurationUnit getEntityConfiguration() {
        return entityConfiguration;
    }

}
