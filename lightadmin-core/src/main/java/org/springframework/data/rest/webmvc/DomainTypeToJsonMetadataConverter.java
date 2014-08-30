package org.springframework.data.rest.webmvc;

import org.lightadmin.core.config.domain.DomainTypeAdministrationConfiguration;
import org.lightadmin.core.config.domain.GlobalAdministrationConfiguration;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.*;

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
                jsonConfigurationMetadata.addAssociationProperty(association);
            }
        });

        if (!globalAdministrationConfiguration.isManagedDomainType(persistentEntity.getType())) {
            return jsonConfigurationMetadata;
        }

        DomainTypeAdministrationConfiguration configuration = globalAdministrationConfiguration.forManagedDomainType(persistentEntity.getType());

        List<DomainConfigurationUnitType> unitTypes = newArrayList(LIST_VIEW, FORM_VIEW, SHOW_VIEW, QUICK_VIEW);

        for (DomainConfigurationUnitType unitType : unitTypes) {
            for (FieldMetadata field : persistentFields(configuration.fieldsForUnit(unitType))) {
                jsonConfigurationMetadata.addPersistentProperty(((PersistentFieldMetadata) field).getPersistentProperty(), unitType);
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
}