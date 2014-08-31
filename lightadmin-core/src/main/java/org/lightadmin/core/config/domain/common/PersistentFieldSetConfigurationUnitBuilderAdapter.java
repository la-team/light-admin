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
package org.lightadmin.core.config.domain.common;

import org.lightadmin.api.config.builder.PersistentFieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.utils.Editors;
import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.view.editor.JspFragmentFieldControl;

import static java.util.Arrays.asList;

public class PersistentFieldSetConfigurationUnitBuilderAdapter implements PersistentFieldSetConfigurationUnitBuilder {

    private final GenericFieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder;

    public PersistentFieldSetConfigurationUnitBuilderAdapter(Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        this.fieldSetConfigurationUnitBuilder = new GenericFieldSetConfigurationUnitBuilder(domainType, configurationUnitType);
    }

    @Override
    public PersistentFieldSetConfigurationUnitBuilder field(final String fieldName) {
        fieldSetConfigurationUnitBuilder.field(fieldName);
        return this;
    }

    @Override
    public PersistentFieldSetConfigurationUnitBuilder caption(final String caption) {
        fieldSetConfigurationUnitBuilder.caption(caption);
        return this;
    }

    @Deprecated
    @Override
    public PersistentFieldSetConfigurationUnitBuilder enumeration(String... values) {
        fieldSetConfigurationUnitBuilder.withCustomControl(Editors.enumeration(values));
        return this;
    }

    @Override
    public PersistentFieldSetConfigurationUnitBuilder enumeration(EnumElement... elements) {
        fieldSetConfigurationUnitBuilder.enumeration(elements);
        fieldSetConfigurationUnitBuilder.withCustomControl(Editors.enumeration(asList(elements)));
        return this;
    }

    @Override
    public PersistentFieldSetConfigurationUnitBuilder editor(JspFragmentFieldControl editControl) {
        fieldSetConfigurationUnitBuilder.withCustomControl(editControl);
        return this;
    }

    @Override
    public FieldSetConfigurationUnit build() {
        return fieldSetConfigurationUnitBuilder.build();
    }
}
