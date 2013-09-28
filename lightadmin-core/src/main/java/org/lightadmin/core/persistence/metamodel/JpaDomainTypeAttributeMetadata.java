package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.lang.annotation.Annotation;

public class JpaDomainTypeAttributeMetadata implements DomainTypeAttributeMetadata {

    private final JpaAttributeMetadata attributeMetadata;

    private Attribute attribute;

    public JpaDomainTypeAttributeMetadata(JpaAttributeMetadata attributeMetadata) {
        this.attributeMetadata = attributeMetadata;
    }

    public JpaDomainTypeAttributeMetadata(EntityType<?> entityType, Attribute attribute) {
        this(new JpaAttributeMetadata(entityType, attribute));
        this.attribute = attribute;
    }

    @Override
    public boolean hasAnnotation(final Class<? extends Annotation> annoType) {
        return attributeMetadata.hasAnnotation(annoType);
    }

    @Override
    public <A extends Annotation> A annotation(final Class<A> annoType) {
        return attributeMetadata.annotation(annoType);
    }

    @Override
    public String getName() {
        return attributeMetadata.name();
    }

    @Override
    public Class<?> getType() {
        return attributeMetadata.type();
    }

    @Override
    public Class<?> getElementType() {
        return attributeMetadata.elementType();
    }

    @Override
    public boolean isCollectionLike() {
        return attributeMetadata.isCollectionLike() || attributeMetadata.isSetLike();
    }

    @Override
    public boolean isSetLike() {
        return attributeMetadata.isSetLike();
    }

    @Override
    public boolean isMapLike() {
        return attributeMetadata.isMapLike();
    }

    @Override
    public boolean isAssociation() {
        switch (attribute.getPersistentAttributeType()) {
            case ONE_TO_ONE:
            case ONE_TO_MANY:
            case MANY_TO_ONE:
            case MANY_TO_MANY:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Object getValue(final Object target) {
        return attributeMetadata.get(target);
    }

    @Override
    public void setValue(Object value, Object target) {
        attributeMetadata.set(value, target);
    }

    public Attribute getAttribute() {
        return attribute;
    }

    @Override
    public Class<?> getAssociationDomainType() {
        if (getAttribute().isCollection()) {
            return getElementType();
        }
        return getType();
    }

    @Override
    public DomainTypeAttributeType getAttributeType() {
        return DomainTypeAttributeType.forAttributeMetadata(this);
    }
}