package org.lightadmin.core.rest;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.LightAdminConfiguration;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.field.Persistable;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.evaluator.FieldValueEvaluator;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType;
import org.lightadmin.core.rest.binary.OperationBuilder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.BeanWrapper;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Collections.EMPTY_SET;
import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.FORM_VIEW;
import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.QUICK_VIEW;
import static org.lightadmin.core.config.domain.configuration.support.ExceptionAwareTransformer.exceptionAwareNameExtractor;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.addPrimaryKeyPersistentField;
import static org.lightadmin.core.rest.binary.OperationBuilder.operationBuilder;

@SuppressWarnings("unchecked")
public class DomainTypeToResourceConverter extends DomainTypeResourceSupport implements Converter<Object, Resource> {

    private final FieldValueEvaluator fieldValueEvaluator = new FieldValueEvaluator();

    private final GlobalAdministrationConfiguration configuration;
    private final OperationBuilder operationBuilder;

    public DomainTypeToResourceConverter(GlobalAdministrationConfiguration configuration, LightAdminConfiguration lightAdminConfiguration, RepositoryRestConfiguration restConfiguration) {
        super(restConfiguration);
        this.configuration = configuration;
        this.operationBuilder = operationBuilder(configuration, lightAdminConfiguration);
    }

    public Resource convert(final Object source, DomainConfigurationUnitType configurationUnitType, Set<FieldMetadata> fieldMetadatas) {
        if (source == null) {
            return null;
        }

        final DomainTypeBasicConfiguration domainTypeConfiguration = configuration.forDomainType(source.getClass());
        if (domainTypeConfiguration == null) {
            return new Resource<Object>(source);
        }

        final PersistentEntity persistentEntity = domainTypeConfiguration.getPersistentEntity();

        BeanWrapper<PersistentEntity<Object, ?>, Object> beanWrapper = BeanWrapper.create(source, null);

        Object id = beanWrapper.getProperty(persistentEntity.getIdProperty());

        final EntityResource entityResource = newEntityResource(domainTypeConfiguration.getDomainTypeName(), id);

        addObjectStringRepresentation(entityResource, domainTypeConfiguration, source);

        addManagedTypeProperty(entityResource, source);

        for (FieldMetadata field : fieldMetadatas) {
            addFieldAttributeValue(entityResource, source, id, field, domainTypeConfiguration, configurationUnitType);
        }

        return entityResource;
    }

    @Override
    public Resource convert(Object source) {
        if (source == null) {
            return null;
        }

        final DomainTypeAdministrationConfiguration domainTypeConfiguration = configuration.forManagedDomainType(source.getClass());
        final DomainTypeBasicConfiguration domainTypeBasicConfig = configuration.forDomainType(source.getClass());

        if (domainTypeConfiguration != null) {
            return convert(source, QUICK_VIEW, primaryKeyFieldOnly(domainTypeConfiguration));
        }

        if (domainTypeBasicConfig != null) {
            return convert(source, QUICK_VIEW, primaryKeyFieldOnly(domainTypeBasicConfig));
        }

        return new Resource<Object>(source);
    }

    private Set<FieldMetadata> primaryKeyFieldOnly(DomainTypeBasicConfiguration domainTypeBasicConfig) {
        return addPrimaryKeyPersistentField(EMPTY_SET, domainTypeBasicConfig.getPersistentEntity().getIdProperty());
    }

    private void addObjectStringRepresentation(final EntityResource resource, final DomainTypeBasicConfiguration configuration, final Object source) {
        final EntityNameExtractor nameExtractor = configuration.getEntityConfiguration().getNameExtractor();

        resource.getContent().put("stringRepresentation", exceptionAwareNameExtractor(nameExtractor, configuration).apply(source));
    }

    private void addManagedTypeProperty(final EntityResource entityResource, final Object source) {
        entityResource.getContent().put("managedDomainType", configuration.forManagedDomainType(source.getClass()) != null);
    }

    private void addFieldAttributeValue(EntityResource resource, Object source, final Object id, FieldMetadata field, final DomainTypeBasicConfiguration domainTypeConfiguration, DomainConfigurationUnitType configurationUnitType) {
        if (FieldMetadataUtils.persistentFieldMetadataPredicate().apply(field)) {
            addAttributeValue(resource, field.getUuid(), persistentFieldData(source, id, (PersistentFieldMetadata) field, domainTypeConfiguration, configurationUnitType));
        } else {
            addAttributeValue(resource, field.getUuid(), transientFieldData(field, source));
        }
    }

    private Map<String, Object> transientFieldData(FieldMetadata field, Object source) {
        final Map<String, Object> fieldData = newLinkedHashMap();

        final Object fieldValue = fieldValueEvaluator.evaluate(field, source);
        final DomainTypeAttributeType type = fieldValue != null ? DomainTypeAttributeType.forType(fieldValue.getClass()) : DomainTypeAttributeType.UNKNOWN;

        fieldData.put("name", field.getName());
        fieldData.put("title", field.getName());
        fieldData.put("value", fieldValue);
        fieldData.put("type", type.name());
        fieldData.put("persistable", false);

        return fieldData;
    }

    private Map<String, Object> persistentFieldData(final Object source, final Object id, final PersistentFieldMetadata field, final DomainTypeBasicConfiguration domainTypeConfiguration, final DomainConfigurationUnitType configurationUnitType) {
        if (DomainTypeAttributeType.forPersistentProperty(field.getPersistentProperty()) == DomainTypeAttributeType.FILE) {
            return persistentFileFieldData(source, id, field, domainTypeConfiguration, configurationUnitType);
        }

        return persistentSimpleFieldData(field, source);
    }

    private Map<String, Object> persistentSimpleFieldData(PersistentFieldMetadata field, Object source) {
        final Map<String, Object> fieldData = newLinkedHashMap();

        Object fieldValue = fieldValueEvaluator.evaluate(field, source);
        fieldData.put("name", field.getField());
        fieldData.put("title", field.getName());
        fieldData.put("value", fieldValue);
        fieldData.put("type", DomainTypeAttributeType.forPersistentProperty(field.getPersistentProperty()).name());
        fieldData.put("persistable", true);
        fieldData.put("primaryKey", field.isPrimaryKey());
        if (field.getRenderer() != null) {
            fieldData.put("label", field.getRenderer().apply(fieldValue));
        }
        return fieldData;
    }

    private Map<String, Object> persistentFileFieldData(Object source, final Object id, PersistentFieldMetadata field, final DomainTypeBasicConfiguration domainTypeConfiguration, final DomainConfigurationUnitType configurationUnitType) {
        final Map<String, Object> fieldData = newLinkedHashMap();
        try {
            boolean fileExists = operationBuilder.fileExistsOperation(source).perform(field.getPersistentProperty());

            fieldData.put("name", field.getField());
            fieldData.put("title", field.getName());
            fieldData.put("type", DomainTypeAttributeType.FILE.name());
            if (configurationUnitType == FORM_VIEW) {
                byte[] fileData = operationBuilder.getOperation(source).perform(field.getPersistentProperty());
                fieldData.put("value", fileData);
            }
            fieldData.put("persistable", true);
            fieldData.put("fileExists", fileExists);
            fieldData.put("fileUrl", filePropertyLink(field, domainTypeConfiguration.getDomainTypeName(), id));
        } catch (IOException e) {
        }
        return fieldData;
    }

    private String filePropertyLink(final Persistable persistable, final String domainTypeName, final Object id) {
        return UriComponentsBuilder.fromUri(restConfiguration.getBaseUri()).pathSegment(domainTypeName).pathSegment(id.toString()).pathSegment(persistable.getField()).pathSegment("file").build().toUri().toString();
    }

    private void addAttributeValue(EntityResource resource, String attributeName, Object value) {
        if (value != null) {
            resource.getContent().put(attributeName, value);
        }
    }

    private EntityResource newEntityResource(String domainTypeName, Object id) {
        final HashSet<Link> links = Sets.newLinkedHashSet();
        links.add(selfLink(domainTypeName, id));
        links.add(selfDomainLink(domainTypeName, id));

        return new EntityResource(Maps.<String, Object>newLinkedHashMap(), links);
    }
}
