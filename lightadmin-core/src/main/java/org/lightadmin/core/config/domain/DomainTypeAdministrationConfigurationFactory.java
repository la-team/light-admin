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

import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.support.Repositories;

import javax.persistence.EntityManager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DomainTypeAdministrationConfigurationFactory {

    private Repositories repositories;
    private EntityManager entityManager;
    private MappingContext<?, ?> mappingContext;

    public DomainTypeAdministrationConfigurationFactory(Repositories repositories, EntityManager entityManager, MappingContext<?, ?> mappingContext) {
        this.entityManager = entityManager;
        this.mappingContext = mappingContext;

        this.repositories = repositories;
    }

    public DomainTypeAdministrationConfiguration createAdministrationConfiguration(ConfigurationUnits configurationUnits) {
        return new DomainTypeAdministrationConfiguration(repositories, configurationUnits);
    }

    public DomainTypeBasicConfiguration createNonManagedDomainTypeConfiguration(Class<?> domainType) {
        JpaRepository repository = new SimpleJpaRepository(domainType, entityManager);

        PersistentEntity persistentEntity = mappingContext.getPersistentEntity(domainType);

        DefaultEntityMetadataConfigurationUnitBuilder builder = new DefaultEntityMetadataConfigurationUnitBuilder(domainType);

        builder.nameExtractor(EntityNameExtractorFactory.forPersistentEntity(persistentEntity));

        return new NonManagedDomainTypeConfiguration(builder.build(), persistentEntity, repository);
    }
}