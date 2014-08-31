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

import org.lightadmin.api.config.builder.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.common.AbstractFieldSetConfigurationBuilder;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

import static org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory.forNamedPersistentEntity;

public class DefaultEntityMetadataConfigurationUnitBuilder extends AbstractFieldSetConfigurationBuilder<EntityMetadataConfigurationUnit, EntityMetadataConfigurationUnitBuilder> implements EntityMetadataConfigurationUnitBuilder {

    private final DefaultEntityMetadataConfigurationUnit configurationUnit;

    public DefaultEntityMetadataConfigurationUnitBuilder(Class<?> domainType) {
        super(domainType);
        configurationUnit = new DefaultEntityMetadataConfigurationUnit(domainType);
        configurationUnit.setSingularName(domainType.getSimpleName());
        configurationUnit.setPluralName(domainType.getSimpleName());
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder nameField(final String nameField) {
        configurationUnit.setNameExtractor(forNamedPersistentEntity(nameField));
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder nameExtractor(final EntityNameExtractor<?> nameExtractor) {
        configurationUnit.setNameExtractor(nameExtractor);
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder singularName(final String singularName) {
        configurationUnit.setSingularName(singularName);
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder pluralName(final String pluralName) {
        configurationUnit.setPluralName(pluralName);
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder repositoryEventListener(Class<? extends AbstractRepositoryEventListener> listenerClass) {
        configurationUnit.setRepositoryEventListener(listenerClass);
        return this;
    }

    @Override
    protected void addCurrentFieldToUnit() {
        if (currentFieldMetadata != null) {
            configurationUnit.addField(currentFieldMetadata);
        }
    }

    @Override
    public EntityMetadataConfigurationUnit build() {
        addCurrentFieldToUnit();
        return configurationUnit;
    }
}
