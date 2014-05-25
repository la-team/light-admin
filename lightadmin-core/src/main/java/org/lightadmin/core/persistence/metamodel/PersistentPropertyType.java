package org.lightadmin.core.persistence.metamodel;

import org.lightadmin.api.config.annotation.FileReference;
import org.springframework.data.mapping.PersistentProperty;

import javax.persistence.Embedded;
import java.util.Date;

import static org.lightadmin.core.util.NumberUtils.isNumberFloat;
import static org.lightadmin.core.util.NumberUtils.isNumberInteger;

public enum PersistentPropertyType {

    STRING,
    NUMBER_INTEGER,
    NUMBER_FLOAT,
    BOOL,
    DATE,
    FILE,
    ASSOC,
    ASSOC_MULTI,
    EMBEDDED,
    MAP,
    UNKNOWN;

    public static boolean isSupportedAttributeType(PersistentPropertyType attributeType) {
        return attributeType != UNKNOWN;
    }

    public static PersistentPropertyType forType(Class<?> attrType) {
        if (Boolean.class.equals(attrType) || boolean.class.equals(attrType)) {
            return BOOL;
        }

        if (Date.class.isAssignableFrom(attrType)) {
            return DATE;
        }

        if (isNumberInteger(attrType)) {
            return NUMBER_INTEGER;
        }

        if (isNumberFloat(attrType)) {
            return NUMBER_FLOAT;
        }

        if (String.class.equals(attrType)) {
            return STRING;
        }

        if (byte[].class.equals(attrType)) {
            return FILE;
        }

        return UNKNOWN;
    }

    public static boolean isOfBinaryFileType(PersistentProperty persistentProperty) {
        return forType(persistentProperty.getType()) == FILE;
    }

    public static boolean isOfFileReferenceType(PersistentProperty persistentProperty) {
        if (forType(persistentProperty.getType()) == STRING && persistentProperty.isAnnotationPresent(FileReference.class)) {
            return true;
        }
        return false;
    }

    public static boolean isOfFileType(PersistentProperty persistentProperty) {
        return isOfBinaryFileType(persistentProperty) || isOfFileReferenceType(persistentProperty);
    }

    public static PersistentPropertyType forPersistentProperty(PersistentProperty persistentProperty) {
        final Class<?> attrType = persistentProperty.getType();

        if (persistentProperty.isAssociation()) {
            if (persistentProperty.isCollectionLike()) {
                return PersistentPropertyType.ASSOC_MULTI;
            }
            return PersistentPropertyType.ASSOC;
        }

        if (persistentProperty.isAnnotationPresent(Embedded.class)) {
            return PersistentPropertyType.EMBEDDED;
        }

        if (persistentProperty.isMap()) {
            return PersistentPropertyType.MAP;
        }

        if (forType(attrType) == STRING && persistentProperty.isAnnotationPresent(FileReference.class)) {
            return PersistentPropertyType.FILE;
        }

        return forType(attrType);
    }
}