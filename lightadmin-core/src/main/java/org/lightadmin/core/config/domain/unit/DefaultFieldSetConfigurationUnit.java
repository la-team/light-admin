package org.lightadmin.core.config.domain.unit;

import com.google.common.collect.FluentIterable;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataUtils;
import org.lightadmin.core.config.domain.field.Persistable;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.persistence.metamodel.PersistentEntityAware;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyAware;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.addPrimaryKeyPersistentField;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.getPersistentField;

public class DefaultFieldSetConfigurationUnit extends DomainTypeConfigurationUnit
        implements FieldSetConfigurationUnit, PersistentEntityAware, HierarchicalConfigurationUnit {

    private static final long serialVersionUID = 1L;

    private final DomainConfigurationUnitType configurationUnitType;

    private Set<FieldMetadata> fields = newLinkedHashSet();

    private ConfigurationUnit parentUnit;

    public DefaultFieldSetConfigurationUnit(Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        super(domainType);
        this.configurationUnitType = configurationUnitType;
    }

    public void addField(FieldMetadata fieldMetadata) {
        fields.add(fieldMetadata);
    }

    public Set<FieldMetadata> getFields() {
        return newLinkedHashSet(fields);
    }

    @Override
    public FieldMetadata getField(String fieldName) {
        return getPersistentField(fields, fieldName);
    }

    @Override
    public Iterator<FieldMetadata> iterator() {
        return getFields().iterator();
    }

    @Override
    public boolean isEmpty() {
        return fields.isEmpty();
    }

    @Override
    public DomainConfigurationUnitType getDomainConfigurationUnitType() {
        return configurationUnitType;
    }

    @Override
    public DomainConfigurationUnitType getParentUnitType() {
        return DomainConfigurationUnitType.CONFIGURATION;
    }

    @Override
    public void setPersistentEntity(PersistentEntity persistenEntity) {
        final PersistentFieldMetadata primaryKeyField = getPersistentField(fields, persistenEntity.getIdProperty().getName());

        if (primaryKeyField != null) {
            primaryKeyField.setPrimaryKey(true);
        } else {
            fields = addPrimaryKeyPersistentField(fields, persistenEntity.getIdProperty());
        }

        for (FieldMetadata field : fields) {
            if (field instanceof PersistentPropertyAware) {
                Persistable persistable = (Persistable) field;
                PersistentProperty persistentProperty = persistenEntity.getPersistentProperty(persistable.getField());
                ((PersistentPropertyAware) field).setPersistentProperty(persistentProperty);
            }
        }
    }

    @Override
    public void setParentUnit(ConfigurationUnit parentUnit) {
        if (parentUnit instanceof FieldSetConfigurationUnit) {
            Set<FieldMetadata> parentDecls = ((FieldSetConfigurationUnit) parentUnit).getFields();
            FluentIterable<PersistentFieldMetadata> localDecls = FluentIterable.from(fields).filter(PersistentFieldMetadata.class);
            for (PersistentFieldMetadata localDecl : localDecls) {
                PersistentFieldMetadata parentDecl = FieldMetadataUtils.getPersistentField(parentDecls, localDecl.getField());
                if (parentDecl != null) {
                    localDecl.inheritFrom(parentDecl);
                }
            }
        }
    }

}
