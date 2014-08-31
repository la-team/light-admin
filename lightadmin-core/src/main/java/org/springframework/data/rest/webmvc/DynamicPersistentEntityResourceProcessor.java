package org.springframework.data.rest.webmvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.lightadmin.core.rest.binary.OperationBuilder;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareNameExtractor;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.customFields;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.transientFields;
import static org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType.*;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.FILE;
import static org.lightadmin.core.rest.binary.OperationBuilder.operationBuilder;
import static org.lightadmin.core.web.util.ApplicationUrlResolver.selfDomainLink;

@SuppressWarnings(value = {"unchecked", "unused"})
public class DynamicPersistentEntityResourceProcessor implements ResourceProcessor<PersistentEntityResource<?>> {

    private final GlobalAdministrationConfiguration adminConfiguration;
    private final OperationBuilder operationBuilder;
    private final EntityLinks entityLinks;

    public DynamicPersistentEntityResourceProcessor(GlobalAdministrationConfiguration adminConfiguration, LightAdminConfiguration lightAdminConfiguration, EntityLinks entityLinks) {
        this.operationBuilder = operationBuilder(adminConfiguration, lightAdminConfiguration);
        this.adminConfiguration = adminConfiguration;
        this.entityLinks = entityLinks;
    }

    @Override
    public PersistentEntityResource<?> process(PersistentEntityResource<?> persistentEntityResource) {
        PersistentEntity persistentEntity = persistentEntityResource.getPersistentEntity();
        Object value = persistentEntityResource.getContent();
        Link[] links = persistentEntityResource.getLinks().toArray(new Link[persistentEntityResource.getLinks().size()]);

        String stringRepresentation = stringRepresentation(value, persistentEntity);
        Link domainLink = domainLink(persistentEntityResource);
        boolean managedDomainType = adminConfiguration.isManagedDomainType(persistentEntity.getType());
        String primaryKey = persistentEntity.getIdProperty().getName();

        Map<DomainConfigurationUnitType, Map<String, Object>> dynamicProperties = dynamicPropertiesPerUnit(value, persistentEntity);

        PersistentEntityWrapper persistentEntityWrapper = new PersistentEntityWrapper(value, dynamicProperties, stringRepresentation, domainLink, managedDomainType, primaryKey);

        return new PersistentEntityResource<>(persistentEntity, persistentEntityWrapper, links);
    }

    private String stringRepresentation(Object value, PersistentEntity persistentEntity) {
        DomainTypeBasicConfiguration domainTypeBasicConfiguration = adminConfiguration.forDomainType(persistentEntity.getType());
        EntityNameExtractor nameExtractor = domainTypeBasicConfiguration.getEntityConfiguration().getNameExtractor();

        return exceptionAwareNameExtractor(nameExtractor, domainTypeBasicConfiguration).apply(value);
    }

    private Link domainLink(PersistentEntityResource persistentEntityResource) {
        PersistentEntity persistentEntity = persistentEntityResource.getPersistentEntity();
        if (adminConfiguration.isManagedDomainType(persistentEntity.getType())) {
            return selfDomainLink(persistentEntityResource, adminConfiguration.forManagedDomainType(persistentEntity.getType()));
        }
        return null;
    }

    private Map<DomainConfigurationUnitType, Map<String, Object>> dynamicPropertiesPerUnit(Object value, PersistentEntity persistentEntity) {
        if (!adminConfiguration.isManagedDomainType(persistentEntity.getType())) {
            return Collections.emptyMap();
        }

        DomainTypeAdministrationConfiguration managedDomainTypeConfiguration = adminConfiguration.forManagedDomainType(persistentEntity.getType());

        List<DomainConfigurationUnitType> units = newArrayList(LIST_VIEW, FORM_VIEW, SHOW_VIEW, QUICK_VIEW);

        List<PersistentProperty> persistentProperties = findPersistentFileProperties(persistentEntity);

        Map<DomainConfigurationUnitType, Map<String, Object>> dynamicPropertiesPerUnit = newHashMap();
        for (DomainConfigurationUnitType unit : units) {
            Map<String, Object> dynamicProperties = newLinkedHashMap();
            for (PersistentProperty persistentProperty : persistentProperties) {
                dynamicProperties.put(persistentProperty.getName(), evaluateFilePropertyValue(persistentProperty, value, managedDomainTypeConfiguration, binaryDataExportNeeded(unit)));
            }
            for (FieldMetadata customField : customFields(managedDomainTypeConfiguration.fieldsForUnit(unit))) {
                dynamicProperties.put(customField.getUuid(), customField.getValue(value));
            }
            for (FieldMetadata transientField : transientFields(managedDomainTypeConfiguration.fieldsForUnit(unit))) {
                dynamicProperties.put(transientField.getUuid(), transientField.getValue(value));
            }
            dynamicPropertiesPerUnit.put(unit, dynamicProperties);
        }
        return dynamicPropertiesPerUnit;
    }

    private List<PersistentProperty> findPersistentFileProperties(PersistentEntity persistentEntity) {
        final List<PersistentProperty> result = newArrayList();
        persistentEntity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> property) {
                if (PersistentPropertyType.forPersistentProperty(property) == FILE) {
                    result.add(property);
                }
            }
        });
        return result;
    }

    private Object evaluateFilePropertyValue(PersistentProperty persistentProperty, Object value, DomainTypeAdministrationConfiguration configuration, boolean binaryDataExportNeeded) {
        try {
            boolean fileExists = operationBuilder.fileExistsOperation(value).perform(persistentProperty);

            if (!fileExists) {
                return new FilePropertyValue(fileExists);
            }

            Link fileLink = new Link(filePropertyLink(persistentProperty, configuration, idValue(value, configuration.getPersistentEntity())));

            if (!binaryDataExportNeeded) {
                return new FilePropertyValue(fileLink);
            }

            byte[] fileData = operationBuilder.getOperation(value).perform(persistentProperty);

            return new FilePropertyValue(fileLink, fileData);

        } catch (Exception e) {
            return null;
        }
    }

    private Object idValue(Object value, PersistentEntity persistentEntity) {
        BeanWrapper<Object> wrapper = BeanWrapper.create(value, null);
        return wrapper.getProperty(persistentEntity.getIdProperty());
    }

    private String filePropertyLink(PersistentProperty persistentProperty, DomainTypeAdministrationConfiguration configuration, Object id) {
        return entityLinks.linkForSingleResource(configuration.getDomainType(), id).slash(persistentProperty.getName()).slash("file").toUri().toString();
    }

    private boolean binaryDataExportNeeded(DomainConfigurationUnitType unit) {
        return unit == FORM_VIEW;
    }

    static class FilePropertyValue {
        private boolean fileExists;
        private Link fileLink;
        private byte[] value;

        FilePropertyValue(boolean fileExists) {
            this.fileExists = fileExists;
        }

        public FilePropertyValue(Link fileLink) {
            this(true);
            this.fileLink = fileLink;
        }

        public FilePropertyValue(Link fileLink, byte[] value) {
            this(fileLink);
            this.value = value;
        }

        @JsonUnwrapped
        @JsonProperty("file_exists")
        public boolean isFileExists() {
            return fileExists;
        }

        @JsonUnwrapped
        @JsonInclude(NON_NULL)
        @JsonProperty("file_link")
        public Link getFileLink() {
            return fileLink;
        }

        @JsonUnwrapped
        @JsonInclude(NON_EMPTY)
        @JsonProperty("value")
        public byte[] getValue() {
            return value;
        }
    }

    static class PersistentEntityWrapper {
        private String stringRepresentation;
        private boolean managedDomainType;
        private String primaryKey;
        private Link domainLink;
        private Object persistentEntity;
        private Map<DomainConfigurationUnitType, Map<String, Object>> dynamicProperties;

        public PersistentEntityWrapper(Object persistentEntity, Map<DomainConfigurationUnitType, Map<String, Object>> dynamicProperties, String stringRepresentation, Link domainLink, boolean managedDomainType, String primaryKey) {
            this.stringRepresentation = stringRepresentation;
            this.domainLink = domainLink;
            this.managedDomainType = managedDomainType;
            this.persistentEntity = persistentEntity;
            this.dynamicProperties = dynamicProperties;
            this.primaryKey = primaryKey;
        }

        @JsonProperty("string_representation")
        public String getStringRepresentation() {
            return stringRepresentation;
        }

        @JsonProperty("primary_key")
        public String getPrimaryKey() {
            return primaryKey;
        }

        @JsonProperty("managed_type")
        public boolean isManagedDomainType() {
            return managedDomainType;
        }

        @JsonProperty("domain_link")
        @JsonInclude(NON_NULL)
        public Link getDomainLink() {
            return domainLink;
        }

        @JsonProperty("original_properties")
        public Object getPersistentEntity() {
            return persistentEntity;
        }

        @JsonProperty("dynamic_properties")
        @JsonInclude(NON_EMPTY)
        public Map<DomainConfigurationUnitType, Map<String, Object>> getDynamicProperties() {
            return dynamicProperties;
        }
    }
}