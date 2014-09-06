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
package org.lightadmin.core.web.json;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.*;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.*;
import static org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType.*;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.EMBEDDED;

public class DomainTypeToJsonMetadataConverter implements Converter<PersistentEntity, JsonConfigurationMetadata> {

    private static final String ID_PLACEHOLDER = "{idPlaceholder}";

    private final GlobalAdministrationConfiguration globalAdministrationConfiguration;
    private final EntityLinks entityLinks;

    public DomainTypeToJsonMetadataConverter(GlobalAdministrationConfiguration globalAdministrationConfiguration, EntityLinks entityLinks) {
        this.globalAdministrationConfiguration = globalAdministrationConfiguration;
        this.entityLinks = entityLinks;
    }

    @Override
    public JsonConfigurationMetadata convert(PersistentEntity persistentEntity) {
        final JsonConfigurationMetadata jsonConfigurationMetadata = new JsonConfigurationMetadata(persistentEntity.getName(), globalAdministrationConfiguration.isManagedDomainType(persistentEntity.getType()));

        persistentEntity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> persistentProperty) {
                jsonConfigurationMetadata.addPersistentProperty(persistentProperty);
            }
        });

        persistentEntity.doWithAssociations(new SimpleAssociationHandler() {
            @Override
            public void doWithAssociation(Association<? extends PersistentProperty<?>> association) {
                jsonConfigurationMetadata.addAssociationProperty(association, associationRestLinkTemplate(association.getInverse()));
            }
        });

        if (!globalAdministrationConfiguration.isManagedDomainType(persistentEntity.getType())) {
            return jsonConfigurationMetadata;
        }

        DomainTypeAdministrationConfiguration configuration = globalAdministrationConfiguration.forManagedDomainType(persistentEntity.getType());

        List<DomainConfigurationUnitType> unitTypes = newArrayList(LIST_VIEW, FORM_VIEW, SHOW_VIEW, QUICK_VIEW);

        for (DomainConfigurationUnitType unitType : unitTypes) {
            Set<FieldMetadata> fieldForUnit = configuration.fieldsForUnit(unitType);

            for (FieldMetadata field : fieldForUnit) {
                if (persistentFieldMetadataPredicate().apply(field)) {
                    addPersistentProperty((PersistentFieldMetadata) field, unitType, jsonConfigurationMetadata);
                }

                if (customFieldMetadataPredicate().apply(field)) {
                    jsonConfigurationMetadata.addDynamicProperty((CustomFieldMetadata) field, unitType);
                }

                if (transientFieldMetadataPredicate().apply(field)) {
                    jsonConfigurationMetadata.addDynamicProperty((TransientFieldMetadata) field, unitType);
                }
            }
        }

        return jsonConfigurationMetadata;
    }

    private void addPersistentProperty(PersistentFieldMetadata field, DomainConfigurationUnitType unitType, JsonConfigurationMetadata jsonConfigurationMetadata) {
        PersistentProperty persistentProperty = field.getPersistentProperty();
        if (persistentProperty.isAssociation()) {
            jsonConfigurationMetadata.addAssociationProperty(field, associationRestLinkTemplate(persistentProperty), unitType);
        } else {
            jsonConfigurationMetadata.addPersistentProperty(field, unitType);
        }
    }

    public Link associationRestLinkTemplate(PersistentProperty persistentProperty) {
        if (PersistentPropertyType.forPersistentProperty(persistentProperty) == EMBEDDED) {
            return null;
        }

        return entityLinks.linkFor(persistentProperty.getActualType()).slash(ID_PLACEHOLDER).withSelfRel();
    }
}