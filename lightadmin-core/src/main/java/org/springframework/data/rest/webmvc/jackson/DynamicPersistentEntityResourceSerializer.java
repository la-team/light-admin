package org.springframework.data.rest.webmvc.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.field.Persistable;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.evaluator.FieldValueEvaluator;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.lightadmin.core.rest.binary.OperationBuilder;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.DynamicPersistentEntityResource;
import org.springframework.data.rest.webmvc.SimpleMapResource;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareNameExtractor;
import static org.lightadmin.core.rest.binary.OperationBuilder.operationBuilder;

class DynamicPersistentEntityResourceSerializer extends StdSerializer<DynamicPersistentEntityResource<?>> {

    private FieldValueEvaluator fieldValueEvaluator;
    private OperationBuilder operationBuilder;

    private GlobalAdministrationConfiguration adminConfiguration;
    private RepositoryRestConfiguration restConfiguration;

    @SuppressWarnings({"unchecked", "rawtypes"})
    DynamicPersistentEntityResourceSerializer(GlobalAdministrationConfiguration globalAdministrationConfiguration, LightAdminConfiguration lightAdminConfiguration, RepositoryRestConfiguration config) {
        super((Class) DynamicPersistentEntityResource.class);

        this.adminConfiguration = globalAdministrationConfiguration;
        this.restConfiguration = config;

        this.operationBuilder = operationBuilder(this.adminConfiguration, lightAdminConfiguration);
        this.fieldValueEvaluator = new FieldValueEvaluator();
    }

    @Override
    public void serialize(final DynamicPersistentEntityResource<?> resource, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        final Link idLink = resource.getId();

        if (idLink == null) {
            throw new JsonGenerationException(String.format("No self link found resource %s!", resource));
        }

        PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity = resource.getPersistentEntity();

        DomainTypeBasicConfiguration domainTypeBasicConfiguration = adminConfiguration.forManagedDomainType(persistentEntity.getType());

        Set<FieldMetadata> fieldMetadatas = resource.getFields();
        Object source = resource.getContent();
        Serializable id = idValue(resource);

        SimpleMapResource entityResource = new SimpleMapResource();
        entityResource.add(resource.getLinks());
        entityResource.add(selfDomainLink(resource, domainTypeBasicConfiguration));

        addObjectStringRepresentation(entityResource, domainTypeBasicConfiguration, source);

        addManagedTypeProperty(entityResource, source);

        for (FieldMetadata field : fieldMetadatas) {
            addFieldAttributeValue(entityResource, source, id, field, domainTypeBasicConfiguration, resource.isExportBinaryData());
        }

        provider.defaultSerializeValue(entityResource, jgen);
    }

    private void addObjectStringRepresentation(final SimpleMapResource resource, final DomainTypeBasicConfiguration configuration, final Object source) {
        final EntityNameExtractor nameExtractor = configuration.getEntityConfiguration().getNameExtractor();

        resource.put("stringRepresentation", exceptionAwareNameExtractor(nameExtractor, configuration).apply(source));
    }

    private void addManagedTypeProperty(final SimpleMapResource resource, final Object source) {
        resource.put("managedDomainType", adminConfiguration.forManagedDomainType(source.getClass()) != null);
    }

    private void addFieldAttributeValue(SimpleMapResource resource, Object source, final Object id, FieldMetadata field, final DomainTypeBasicConfiguration domainTypeConfiguration, boolean exportBinaryData) {
        if (FieldMetadataUtils.persistentFieldMetadataPredicate().apply(field)) {
            addAttributeValue(resource, field.getUuid(), persistentFieldData(source, id, (PersistentFieldMetadata) field, domainTypeConfiguration, exportBinaryData));
        } else {
            addAttributeValue(resource, field.getUuid(), transientFieldData(field, source));
        }
    }

    private Map<String, Object> transientFieldData(FieldMetadata field, Object source) {
        final Map<String, Object> fieldData = newLinkedHashMap();

        final Object fieldValue = fieldValueEvaluator.evaluate(field, source);
        final PersistentPropertyType type = fieldValue != null ? PersistentPropertyType.forType(fieldValue.getClass()) : PersistentPropertyType.UNKNOWN;

        fieldData.put("name", field.getName());
        fieldData.put("title", field.getName());
        fieldData.put("value", fieldValue);
        fieldData.put("type", type.name());
        fieldData.put("persistable", false);

        return fieldData;
    }

    private Map<String, Object> persistentFieldData(final Object source, final Object id, final PersistentFieldMetadata field, final DomainTypeBasicConfiguration domainTypeConfiguration, boolean exportBinaryData) {
        if (PersistentPropertyType.forPersistentProperty(field.getPersistentProperty()) == PersistentPropertyType.FILE) {
            return persistentFileFieldData(source, id, field, domainTypeConfiguration, exportBinaryData);
        }

        return persistentSimpleFieldData(field, source);
    }

    private Map<String, Object> persistentSimpleFieldData(PersistentFieldMetadata field, Object source) {
        final Map<String, Object> fieldData = newLinkedHashMap();

        Object fieldValue = fieldValueEvaluator.evaluate(field, source);
        fieldData.put("name", field.getField());
        fieldData.put("title", field.getName());
        fieldData.put("value", fieldValue);
        fieldData.put("type", PersistentPropertyType.forPersistentProperty(field.getPersistentProperty()).name());
        fieldData.put("persistable", true);
        fieldData.put("primaryKey", field.isPrimaryKey());
        if (field.getRenderer() != null) {
            fieldData.put("label", field.getRenderer().apply(fieldValue));
        }
        return fieldData;
    }

    private Map<String, Object> persistentFileFieldData(Object source, final Object id, PersistentFieldMetadata field, final DomainTypeBasicConfiguration domainTypeConfiguration, final boolean exportBinaryData) {
        final Map<String, Object> fieldData = newLinkedHashMap();
        try {
            boolean fileExists = operationBuilder.fileExistsOperation(source).perform(field.getPersistentProperty());

            fieldData.put("name", field.getField());
            fieldData.put("title", field.getName());
            fieldData.put("type", PersistentPropertyType.FILE.name());
            if (exportBinaryData) {
                byte[] fileData = operationBuilder.getOperation(source).perform(field.getPersistentProperty());
                fieldData.put("value", fileData);
            }
            fieldData.put("persistable", true);
            fieldData.put("fileExists", fileExists);
            fieldData.put("fileUrl", filePropertyLink(field, domainTypeConfiguration.getPluralDomainTypeName(), id));
        } catch (IOException e) {
        }
        return fieldData;
    }

    private String filePropertyLink(final Persistable persistable, final String domainTypeName, final Object id) {
        return UriComponentsBuilder.fromUri(restConfiguration.getBaseUri()).pathSegment(domainTypeName).pathSegment(id.toString()).pathSegment(persistable.getField()).pathSegment("file").build().toUri().toString();
    }

    private void addAttributeValue(SimpleMapResource resource, String attributeName, Object value) {
        if (value != null) {
            resource.put(attributeName, value);
        }
    }

    private Link selfDomainLink(DynamicPersistentEntityResource<?> resource, DomainTypeBasicConfiguration domainTypeBasicConfiguration) {
        UriComponentsBuilder selfUriBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .pathSegment("domain")
                .pathSegment(domainTypeBasicConfiguration.getDomainTypeName())
                .pathSegment(idValue(resource).toString());

        return new Link(selfUriBuilder.build().toString(), "selfDomainLink");
    }

    private Serializable idValue(DynamicPersistentEntityResource<?> resource) {
        BeanWrapper beanWrapper = BeanWrapper.create(resource.getContent(), null);
        return (Serializable) beanWrapper.getProperty(resource.getPersistentEntity().getIdProperty());
    }
}