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
package org.lightadmin.core.config.domain.unit.processor;

import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.common.GenericFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.springframework.data.mapping.*;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.ClassUtils;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType.CONFIGURATION;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.isSupportedAttributeType;

public class EmptyConfigurationUnitPostProcessor extends MappingContextAwareConfigurationUnitPostProcessor {

    public EmptyConfigurationUnitPostProcessor(final MappingContext mappingContext) {
        super(mappingContext);
    }

    @Override
    public ConfigurationUnit postProcess(final ConfigurationUnit configurationUnit, ConfigurationUnits configurationUnits) {
        if (configurationUnit.getDomainConfigurationUnitType() != CONFIGURATION && isEmptyFieldSetConfigurationUnit(configurationUnit)) {
            return fieldSetUnitWithPersistentFields(configurationUnit.getDomainType(), configurationUnit.getDomainConfigurationUnitType());
        }

        return configurationUnit;
    }

    private FieldSetConfigurationUnit fieldSetUnitWithPersistentFields(final Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        final FieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder = new GenericFieldSetConfigurationUnitBuilder(domainType, configurationUnitType);

        PersistentEntity persistentEntity = getPersistentEntity(domainType);

        persistentEntity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> property) {
                addField(property, fieldSetConfigurationUnitBuilder);
            }
        });

        persistentEntity.doWithAssociations(new SimpleAssociationHandler() {
            @Override
            public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                addField(association.getInverse(), fieldSetConfigurationUnitBuilder);
            }
        });

        return fieldSetConfigurationUnitBuilder.build();
    }

    private void addField(PersistentProperty<?> property, FieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder) {
        if (isSupportedAttributeType(PersistentPropertyType.forPersistentProperty(property))) {
            fieldSetConfigurationUnitBuilder.field(property.getName()).caption(capitalize(property.getName()));
        }
    }

    private boolean isEmptyFieldSetConfigurationUnit(ConfigurationUnit configurationUnit) {
        return ClassUtils.isAssignableValue(FieldSetConfigurationUnit.class, configurationUnit) && ((FieldSetConfigurationUnit) configurationUnit).isEmpty();
    }
}
