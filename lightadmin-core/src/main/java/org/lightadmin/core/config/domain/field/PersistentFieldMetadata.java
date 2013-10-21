package org.lightadmin.core.config.domain.field;

import org.hibernate.validator.constraints.NotBlank;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.*;

public class PersistentFieldMetadata extends AbstractFieldMetadata implements DomainTypeAttributeMetadataAware, Persistable {

    private final String field;

    private boolean primaryKey;

    private DomainTypeAttributeMetadata attributeMetadata;

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
        return attributeMetadata.hasAnnotation(NotNull.class)
                || attributeMetadata.hasAnnotation(NotBlank.class)
                || attributeMetadata.hasAnnotation(org.hibernate.validator.constraints.NotEmpty.class)
                || (attributeMetadata.hasAnnotation(Column.class) && !attributeMetadata.annotation(Column.class).nullable()
                || (attributeMetadata.hasAnnotation(Basic.class) && !attributeMetadata.annotation(Basic.class).optional())
                || (attributeMetadata.hasAnnotation(ManyToOne.class) && !attributeMetadata.annotation(ManyToOne.class).optional())
                || (attributeMetadata.hasAnnotation(OneToOne.class) && !attributeMetadata.annotation(OneToOne.class).optional())
        );
    }

    @Override
    public boolean isGeneratedValue() {
        return attributeMetadata.hasAnnotation(GeneratedValue.class);
    }

    public void setPrimaryKey(final boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public void setAttributeMetadata(final DomainTypeAttributeMetadata attributeMetadata) {
        this.attributeMetadata = attributeMetadata;
    }

    @Override
    public DomainTypeAttributeMetadata getAttributeMetadata() {
        return attributeMetadata;
    }

    @Override
    public boolean isSortable() {
        return this.attributeMetadata.getAttributeType() != ASSOC && this.attributeMetadata.getAttributeType() != ASSOC_MULTI && this.attributeMetadata.getAttributeType() != FILE;
    }

    @Override
    public String getUuid() {
        return field;
    }
}
