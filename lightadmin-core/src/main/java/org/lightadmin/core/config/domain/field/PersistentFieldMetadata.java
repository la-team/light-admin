package org.lightadmin.core.config.domain.field;

import org.hibernate.validator.constraints.NotBlank;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;
import org.springframework.data.mapping.PersistentProperty;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;

public class PersistentFieldMetadata extends AbstractFieldMetadata implements DomainTypeAttributeMetadataAware, Persistable {

    private final String field;

    private boolean primaryKey;

    private PersistentProperty persistentProperty;

    public PersistentFieldMetadata(final String name, final String field, boolean primaryKey) {
        this(name, field);
        this.primaryKey = primaryKey;
    }

    public PersistentFieldMetadata(final String name, final String field) {
        super(name);
        this.field = field;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    @Override
    public boolean isRequired() {
        return persistentProperty.isAnnotationPresent(NotNull.class)
                || persistentProperty.isAnnotationPresent(NotBlank.class)
                || persistentProperty.isAnnotationPresent(org.hibernate.validator.constraints.NotEmpty.class);
    }

    @Override
    public boolean isGeneratedValue() {
        return persistentProperty.isAnnotationPresent(GeneratedValue.class);
    }

    public void setPrimaryKey(final boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public void setAttributeMetadata(final PersistentProperty persistentProperty) {
        this.persistentProperty = persistentProperty;
    }

    @Override
    public PersistentProperty getPersistentProperty() {
        return persistentProperty;
    }

    @Override
    public boolean isSortable() {
        return !this.persistentProperty.isAssociation();

//        return this.attributeMetadata.getAttributeType() != ASSOC && this.attributeMetadata.getAttributeType() != ASSOC_MULTI && this.attributeMetadata.getAttributeType() != FILE;
    }

    @Override
    public String getUuid() {
        return field;
    }

}
