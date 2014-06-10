package org.lightadmin.core.config.domain.unit.visitor;

import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.unit.handler.FieldHandler;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.getPersistentField;
import static org.lightadmin.core.config.domain.field.PersistentFieldMetadata.keyField;

public class FieldSetConfigurationUnitVisitor extends ParametrizedConfigurationUnitVisitor<DefaultFieldSetConfigurationUnit> {

    private PersistentEntity persistentEntity;

    public FieldSetConfigurationUnitVisitor(PersistentEntity persistentEntity) {
        this.persistentEntity = persistentEntity;
    }

    @Override
    protected void visitInternal(DefaultFieldSetConfigurationUnit configurationUnit) {
        PersistentProperty idProperty = persistentEntity.getIdProperty();
        PersistentFieldMetadata primaryKeyField = getPersistentField(configurationUnit.getFields(), idProperty.getName());

        if (primaryKeyField != null) {
            primaryKeyField.setPrimaryKey(true);
        } else {
            configurationUnit.addField(keyField(capitalize(idProperty.getName()), idProperty.getName()));
        }

        configurationUnit.doWithPersistentFields(new FieldHandler<PersistentFieldMetadata>() {
            @Override
            public void doWithField(PersistentFieldMetadata persistentField) {
                PersistentProperty persistentProperty = persistentEntity.getPersistentProperty(persistentField.getField());
                persistentField.setPersistentProperty(persistentProperty);
            }
        });
    }
}