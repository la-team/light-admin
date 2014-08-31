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

import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.unit.handler.FieldHandler;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.getPersistentField;
import static org.lightadmin.core.config.domain.field.PersistentFieldMetadata.keyField;

public class FieldSetConfigurationUnitVisitor extends ParametrizedConfigurationUnitVisitor<DefaultFieldSetConfigurationUnit> {

    private PersistentEntity persistentEntity;

    public FieldSetConfigurationUnitVisitor(PersistentEntity persistentEntity) {
        this.persistentEntity = persistentEntity;
    }

    @Override
    protected void visitInternal(DefaultFieldSetConfigurationUnit configurationUnit) {
        PersistentProperty idProperty = persistentEntity.getIdProperty();
        PersistentFieldMetadata primaryKeyField = getPersistentField(configurationUnit.getFields(), idProperty.getName());

        if (primaryKeyField != null) {
            primaryKeyField.setPrimaryKey(true);
        } else {
            configurationUnit.addField(keyField(capitalize(idProperty.getName()), idProperty.getName()));
        }

        configurationUnit.doWithPersistentFields(new FieldHandler<PersistentFieldMetadata>() {
            @Override
            public void doWithField(PersistentFieldMetadata persistentField) {
                PersistentProperty persistentProperty = persistentEntity.getPersistentProperty(persistentField.getField());
                persistentField.setPersistentProperty(persistentProperty);
            }
        });
    }
}