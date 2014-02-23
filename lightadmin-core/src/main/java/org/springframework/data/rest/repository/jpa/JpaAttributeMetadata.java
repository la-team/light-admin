package org.springframework.data.rest.repository.jpa;

import org.springframework.data.rest.repository.AttributeMetadata;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.lang.annotation.Annotation;

public class JpaAttributeMetadata implements AttributeMetadata {
    public JpaAttributeMetadata(EntityType<?> entityType, Attribute attribute) {

    }


    @Override
    public boolean hasAnnotation(Class<? extends Annotation> annoType) {
        return false;
    }

    @Override
    public <A extends Annotation> A annotation(Class<A> annoType) {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public Class<?> type() {
        return null;
    }

    @Override
    public Class<?> elementType() {
        return null;
    }

    @Override
    public boolean isCollectionLike() {
        return false;
    }

    @Override
    public boolean isSetLike() {
        return false;
    }

    @Override
    public boolean isMapLike() {
        return false;
    }

    @Override
    public Object get(Object target) {
        return null;
    }

    @Override
    public void set(Object value, Object target) {

    }
}