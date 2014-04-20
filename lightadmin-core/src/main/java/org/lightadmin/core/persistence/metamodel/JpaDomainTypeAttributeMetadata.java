package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.rest.repository.AttributeMetadata;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import java.lang.annotation.Annotation;

public class JpaDomainTypeAttributeMetadata implements DomainTypeAttributeMetadata {

    private Attribute attribute;

    public JpaDomainTypeAttributeMetadata(EntityType<?> entityType, Attribute attribute) {
        this.attribute = attribute;
    }

    @Override
    public boolean hasAnnotation(final Class<? extends Annotation> annoType) {
//        return attributeMetadata.hasAnnotation(annoType);
        return false;
    }

    @Override
    public <A extends Annotation> A annotation(final Class<A> annoType) {
//        return attributeMetadata.annotation(annoType);
        return null;
    }

    @Override
    public AttributeMetadata getAttributeMetadata() {
        throw new RuntimeException("No Attribute metadata found!");
    }

    @Override
    public String getName() {
        return attribute.getName();
    }

    @Override
    public Class<?> getType() {
        return attribute.getJavaType();
    }

    @Override
    public Class<?> getElementType() {
        return ((PluralAttribute) attribute).getElementType().getJavaType();
    }

    @Override
    public boolean isCollectionLike() {
//        return attributeMetadata.isCollectionLike() || attributeMetadata.isSetLike();
        return false;
    }

    @Override
    public boolean isSetLike() {
//        return attributeMetadata.isSetLike();
        return false;
    }

    @Override
    public boolean isMapLike() {
//        return attributeMetadata.isMapLike();
        return false;
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
//        return attributeMetadata.get(target);
        return null;
    }

    @Override
    public void setValue(Object value, Object target) {
//        attributeMetadata.set(value, target);

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