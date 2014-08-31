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

import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;

import static org.lightadmin.core.config.domain.field.FieldMetadataFactory.customField;
import static org.lightadmin.core.config.domain.field.FieldMetadataFactory.transientField;

public class GenericFieldSetConfigurationUnitBuilder extends AbstractFieldSetConfigurationBuilder<FieldSetConfigurationUnit, FieldSetConfigurationUnitBuilder>
        implements FieldSetConfigurationUnitBuilder {

    private final FieldSetConfigurationUnit configurationUnit;

    public GenericFieldSetConfigurationUnitBuilder(Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        super(domainType);

        this.configurationUnit = new DefaultFieldSetConfigurationUnit(domainType, configurationUnitType);
    }

    @Override
    public FieldSetConfigurationUnitBuilder dynamic(final String expression) {
        addCurrentFieldToUnit();

        currentFieldMetadata = transientField("Undefined", expression);

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FieldSetConfigurationUnitBuilder renderable(final FieldValueRenderer renderer) {
        addCurrentFieldToUnit();

        currentFieldMetadata = customField("Undefined", renderer);

        return this;
    }

    @Override
    public FieldSetConfigurationUnit build() {
        addCurrentFieldToUnit();

        return configurationUnit;
    }


    @Override
    protected void addCurrentFieldToUnit() {
        if (currentFieldMetadata != null) {
            configurationUnit.addField(currentFieldMetadata);
        }
    }
}
