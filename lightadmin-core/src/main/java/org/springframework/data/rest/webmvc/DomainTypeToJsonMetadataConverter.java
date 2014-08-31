package org.springframework.data.rest.webmvc;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.DomainTypeBasicConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.*;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.*;
import static org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType.*;

public class DomainTypeToJsonMetadataConverter implements Converter<PersistentEntity, JsonConfigurationMetadata> {

    private final GlobalAdministrationConfiguration globalAdministrationConfiguration;

    public DomainTypeToJsonMetadataConverter(GlobalAdministrationConfiguration globalAdministrationConfiguration) {
        this.globalAdministrationConfiguration = globalAdministrationConfiguration;
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
            for (FieldMetadata field : persistentFields(configuration.fieldsForUnit(unitType))) {
                PersistentProperty persistentProperty = ((PersistentFieldMetadata) field).getPersistentProperty();
                if (persistentProperty.isAssociation()) {
                    jsonConfigurationMetadata.addAssociationProperty(persistentProperty.getAssociation(), associationRestLinkTemplate(persistentProperty), unitType);
                } else {
                    jsonConfigurationMetadata.addPersistentProperty(persistentProperty, unitType);
                }
            }

            for (FieldMetadata customField : customFields(configuration.fieldsForUnit(unitType))) {
                jsonConfigurationMetadata.addDynamicProperty((CustomFieldMetadata) customField, unitType);
            }

            for (FieldMetadata transientField : transientFields(configuration.fieldsForUnit(unitType))) {
                jsonConfigurationMetadata.addDynamicProperty((TransientFieldMetadata) transientField, unitType);
            }
        }

        return jsonConfigurationMetadata;
    }

    public Link associationRestLinkTemplate(PersistentProperty persistentProperty) {
        if (PersistentPropertyType.forPersistentProperty(persistentProperty) == PersistentPropertyType.EMBEDDED) {
            return null;
        }

        DomainTypeBasicConfiguration domainTypeBasicConfiguration = globalAdministrationConfiguration.forDomainType(persistentProperty.getActualType());
        UriComponentsBuilder selfUriBuilder = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .pathSegment("rest")
                .pathSegment(domainTypeBasicConfiguration.getPluralDomainTypeName())
                .pathSegment("{idPlaceholder}");

        return new Link(selfUriBuilder.build().toString(), "self");
    }
}