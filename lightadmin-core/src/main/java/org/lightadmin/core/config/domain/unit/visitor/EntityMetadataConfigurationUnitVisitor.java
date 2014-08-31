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
package org.lightadmin.core.config.domain.unit.visitor;

import org.lightadmin.core.config.domain.configuration.DefaultEntityMetadataConfigurationUnit;
import org.springframework.data.mapping.PersistentEntity;

import static org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory.forPersistentEntity;

public class EntityMetadataConfigurationUnitVisitor extends ParametrizedConfigurationUnitVisitor<DefaultEntityMetadataConfigurationUnit> {

    private PersistentEntity persistenEntity;

    public EntityMetadataConfigurationUnitVisitor(PersistentEntity persistenEntity) {
        this.persistenEntity = persistenEntity;
    }

    @Override
    protected void visitInternal(DefaultEntityMetadataConfigurationUnit configurationUnit) {
        if (configurationUnit.getNameExtractor() == null) {
            configurationUnit.setNameExtractor(forPersistentEntity(configurationUnit.getSingularName(), this.persistenEntity));
        }
    }
}