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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
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

        final List<PersistentProperty<?>> persistentProperties = persistentPropertiesOf(persistentEntity);

        Collections.sort(persistentProperties, new PersistentPropertyComparator());

        for (PersistentProperty<?> persistentProperty : persistentProperties) {
            addField(persistentProperty, fieldSetConfigurationUnitBuilder);
        }

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

    private List<PersistentProperty<?>> persistentPropertiesOf(PersistentEntity persistentEntity) {
        final List<PersistentProperty<?>> persistentProperties = newArrayList();

        persistentEntity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> property) {
                persistentProperties.add(property);
            }
        });

        persistentEntity.doWithAssociations(new SimpleAssociationHandler() {
            @Override
            public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                persistentProperties.add(association.getInverse());
            }
        });

        return persistentProperties;
    }

    private static class PersistentPropertyComparator implements Comparator<PersistentProperty> {
        @Override
        public int compare(final PersistentProperty persistentProperty1, final PersistentProperty persistentProperty2) {
            if (isPrimaryKey(persistentProperty1)) {
                return -1;
            }
            if (isPrimaryKey(persistentProperty2)) {
                return 1;
            }
            return persistentProperty1.getName().compareTo(persistentProperty2.getName());
        }

        private boolean isPrimaryKey(PersistentProperty persistentProperty) {
            return persistentProperty.isIdProperty();
        }
    }
}
