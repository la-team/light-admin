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

import com.google.common.collect.FluentIterable;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.HierarchicalConfigurationUnit;

import java.util.Set;

public class HierarchicalConfigurationUnitVisitor extends ParametrizedConfigurationUnitVisitor<HierarchicalConfigurationUnit> {

    private ConfigurationUnit parentUnit;

    public HierarchicalConfigurationUnitVisitor(ConfigurationUnit parentUnit) {
        this.parentUnit = parentUnit;
    }

    @Override
    protected void visitInternal(HierarchicalConfigurationUnit configurationUnit) {
        if (parentUnit instanceof FieldSetConfigurationUnit && configurationUnit instanceof FieldSetConfigurationUnit) {
            Set<FieldMetadata> parentDecls = ((FieldSetConfigurationUnit) parentUnit).getFields();

            FluentIterable<PersistentFieldMetadata> localDecls = FluentIterable.from(((FieldSetConfigurationUnit) configurationUnit).getFields()).filter(PersistentFieldMetadata.class);
            for (PersistentFieldMetadata localDecl : localDecls) {
                PersistentFieldMetadata parentDecl = FieldMetadataUtils.getPersistentField(parentDecls, localDecl.getField());
                if (parentDecl != null) {
                    localDecl.inheritFrom(parentDecl);
                }
            }
        }
    }
}