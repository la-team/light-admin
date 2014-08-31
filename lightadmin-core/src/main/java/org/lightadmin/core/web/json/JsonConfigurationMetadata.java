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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.Maps;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.STRING;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.UNKNOWN;

@SuppressWarnings("unused")
@JsonPropertyOrder(value = {"name", "managed_type", "original_properties", "dynamic_properties"})
public class JsonConfigurationMetadata extends Resource<Map<String, JsonConfigurationMetadata.Property>> {

    private String name;
    private boolean managedDomainType;

    private Map<DomainConfigurationUnitType, Map<String, Property>> dynamicProperties;

    public JsonConfigurationMetadata(String name, boolean managedDomainType) {
        super(Maps.<String, JsonConfigurationMetadata.Property>newLinkedHashMap());

        this.name = name;
        this.managedDomainType = managedDomainType;
        this.dynamicProperties = newLinkedHashMap();
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("managed_type")
    public boolean isManagedDomainType() {
        return managedDomainType;
    }

    @JsonProperty("dynamic_properties")
    @JsonInclude(NON_EMPTY)
    public Map<DomainConfigurationUnitType, Map<String, Property>> getDynamicProperties() {
        return dynamicProperties;
    }

    @Override
    @JsonProperty("original_properties")
    public Map<String, JsonConfigurationMetadata.Property> getContent() {
        return super.getContent();
    }

    public JsonConfigurationMetadata addAssociationProperty(Association association, Link restTemplateLink) {
        PersistentProperty persistentProperty = association.getInverse();

        addProperty(persistentProperty.getName(), newAssociationProperty(persistentProperty, persistentProperty.getName(), restTemplateLink));

        return this;
    }

    public JsonConfigurationMetadata addAssociationProperty(Association association, Link restTemplateLink, DomainConfigurationUnitType unitType) {
        PersistentProperty persistentProperty = association.getInverse();
        String persistentPropertyName = persistentProperty.getName();

        addDynamicProperty(persistentPropertyName, newAssociationProperty(persistentProperty, persistentPropertyName, restTemplateLink), unitType);

        return this;
    }

    public JsonConfigurationMetadata addAssociationProperty(PersistentFieldMetadata persistentFieldMetadata, Link restTemplateLink, DomainConfigurationUnitType unitType) {
        PersistentProperty persistentProperty = persistentFieldMetadata.getPersistentProperty().getAssociation().getInverse();
        String persistentPropertyName = persistentProperty.getName();
        String persistentPropertyTitle = persistentFieldMetadata.getName();

        addDynamicProperty(persistentPropertyName, newAssociationProperty(persistentProperty, persistentPropertyTitle, restTemplateLink), unitType);

        return this;
    }

    public JsonConfigurationMetadata addPersistentProperty(PersistentProperty persistentProperty) {
        String persistentPropertyName = persistentProperty.getName();

        addProperty(persistentPropertyName, newProperty(persistentProperty, persistentPropertyName));

        return this;
    }

    public JsonConfigurationMetadata addPersistentProperty(PersistentProperty persistentProperty, DomainConfigurationUnitType unitType) {
        String persistentPropertyName = persistentProperty.getName();

        addDynamicProperty(persistentPropertyName, newProperty(persistentProperty, persistentPropertyName), unitType);

        return this;
    }

    public JsonConfigurationMetadata addPersistentProperty(PersistentFieldMetadata persistentField, DomainConfigurationUnitType unitType) {
        PersistentProperty persistentProperty = persistentField.getPersistentProperty();
        String persistentPropertyName = persistentProperty.getName();
        String persistentPropertyTitle = persistentField.getName();

        addDynamicProperty(persistentPropertyName, newProperty(persistentProperty, persistentPropertyTitle), unitType);

        return this;
    }

    public JsonConfigurationMetadata addDynamicProperty(TransientFieldMetadata transientField, DomainConfigurationUnitType unitType) {
        Property property = new Property(transientField.getUuid(), transientField.getName(), UNKNOWN, false, false);

        addDynamicProperty(transientField.getUuid(), property, unitType);

        return this;
    }

    public JsonConfigurationMetadata addDynamicProperty(CustomFieldMetadata customField, DomainConfigurationUnitType unitType) {
        Property property = new Property(customField.getUuid(), customField.getName(), STRING, false, false);

        addDynamicProperty(customField.getUuid(), property, unitType);

        return this;
    }

    private JsonConfigurationMetadata addDynamicProperty(String name, Property property, DomainConfigurationUnitType configurationUnitType) {
        if (!dynamicProperties.containsKey(configurationUnitType)) {
            dynamicProperties.put(configurationUnitType, Maps.<String, Property>newLinkedHashMap());
        }
        dynamicProperties.get(configurationUnitType).put(name, property);
        return this;
    }

    private JsonConfigurationMetadata addProperty(String name, JsonConfigurationMetadata.Property property) {
        getContent().put(name, property);
        return this;
    }

    private JsonConfigurationMetadata.Property newAssociationProperty(PersistentProperty persistentProperty, String title, Link restTemplateLink) {
        PersistentPropertyType type = PersistentPropertyType.forPersistentProperty(persistentProperty);
        return new JsonConfigurationMetadata.AssociationProperty(persistentProperty.getName(), title, type, true, false, restTemplateLink);
    }

    private JsonConfigurationMetadata.Property newProperty(PersistentProperty persistentProperty, String title) {
        PersistentPropertyType type = PersistentPropertyType.forPersistentProperty(persistentProperty);
        return new JsonConfigurationMetadata.Property(persistentProperty.getName(), title, type, true, persistentProperty.isIdProperty());
    }

    static class AssociationProperty extends JsonConfigurationMetadata.Property {

        private Link restLink;

        public AssociationProperty(String name, String title, PersistentPropertyType propertyType, boolean persistable, boolean primaryKey, Link restLink) {
            super(name, title, propertyType, persistable, primaryKey);
            this.restLink = restLink;
        }

        @JsonProperty("rest_link")
        @JsonInclude(NON_NULL)
        public Link getRestLink() {
            return restLink;
        }
    }

    static class Property {
        private String name;
        private String title;
        private String type;
        private boolean persistable;
        private boolean primaryKey;

        public Property(String name, String title, PersistentPropertyType propertyType, boolean persistable, boolean primaryKey) {
            this.name = name;
            this.title = title;
            this.type = propertyType.name();
            this.persistable = persistable;
            this.primaryKey = primaryKey;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public boolean isPersistable() {
            return persistable;
        }

        public boolean isPrimaryKey() {
            return primaryKey;
        }
    }
}