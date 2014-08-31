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
package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

public class DefaultEntityMetadataConfigurationUnit extends DefaultFieldSetConfigurationUnit implements EntityMetadataConfigurationUnit {

    private EntityNameExtractor<?> nameExtractor;
    private Class<? extends AbstractRepositoryEventListener> repositoryEventListener;

    private String singularName;
    private String pluralName;

    DefaultEntityMetadataConfigurationUnit(Class<?> domainType) {
        super(domainType, DomainConfigurationUnitType.CONFIGURATION);
    }

    @Override
    public Class<? extends AbstractRepositoryEventListener> getRepositoryEventListener() {
        return repositoryEventListener;
    }

    @Override
    public EntityNameExtractor getNameExtractor() {
        return nameExtractor;
    }

    @Override
    public String getSingularName() {
        return this.singularName;
    }

    @Override
    public String getPluralName() {
        return this.pluralName;
    }

    public void setNameExtractor(EntityNameExtractor<?> nameExtractor) {
        this.nameExtractor = nameExtractor;
    }

    public void setSingularName(String singularName) {
        this.singularName = singularName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public void setRepositoryEventListener(Class<? extends AbstractRepositoryEventListener> repositoryEventListener) {
        this.repositoryEventListener = repositoryEventListener;
    }

    @Override
    public DomainConfigurationUnitType getParentUnitType() {
        return null;
    }

}