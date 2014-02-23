package org.springframework.data.rest.repository;

import java.lang.annotation.Annotation;

public interface AttributeMetadata {
    boolean hasAnnotation(Class<? extends Annotation> annoType);

    <A extends Annotation> A annotation(Class<A> annoType);

    String name();

    Class<?> type();

    Class<?> elementType();

    boolean isCollectionLike();

    boolean isSetLike();

    boolean isMapLike();

    Object get(Object target);

    void set(Object value, Object target);
}