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

import org.lightadmin.api.config.unit.FiltersConfigurationUnit;
import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.lightadmin.core.config.domain.unit.handler.FilterHandler;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

public class FiltersConfigurationUnitVisitor extends ParametrizedConfigurationUnitVisitor<FiltersConfigurationUnit> {

    private PersistentEntity persistentEntity;

    public FiltersConfigurationUnitVisitor(PersistentEntity persistentEntity) {
        this.persistentEntity = persistentEntity;
    }

    @Override
    protected void visitInternal(FiltersConfigurationUnit configurationUnit) {
        configurationUnit.doWithFilters(new FilterHandler<FilterMetadata>() {
            @Override
            public void doWithFilter(FilterMetadata filter) {
                PersistentProperty persistentProperty = persistentEntity.getPersistentProperty(filter.getFieldName());
                filter.setPersistentProperty(persistentProperty);
            }
        });
    }
}