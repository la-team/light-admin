/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lightadmin.core.persistence.metamodel;

import org.lightadmin.api.config.annotation.FileReference;
import org.springframework.data.mapping.PersistentProperty;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
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

        if (persistentProperty.isAnnotationPresent(Embedded.class) || persistentProperty.isAnnotationPresent(EmbeddedId.class)) {
            return PersistentPropertyType.EMBEDDED;
        }

        if (persistentProperty.isAssociation()) {
            if (persistentProperty.isCollectionLike()) {
                return PersistentPropertyType.ASSOC_MULTI;
            }
            return PersistentPropertyType.ASSOC;
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