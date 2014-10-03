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

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.lightadmin.api.config.annotation.FileReference;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.util.ClassUtils;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.lightadmin.core.util.NumberUtils.isNumberFloat;
import static org.lightadmin.core.util.NumberUtils.isNumberInteger;

public enum PersistentPropertyType {

    STRING,
    NUMBER_INTEGER,
    NUMBER_FLOAT,
    BOOL,
    DATE,
    TIME,
    DATE_TIME,
    FILE,
    ASSOC,
    ASSOC_MULTI,
    ENUM,
    EMBEDDED,
    MAP,
    UNKNOWN;

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

        if (ClassUtils.isAssignable(Enum.class, attrType)) {
            return ENUM;
        }

        if (forType(attrType) == STRING && persistentProperty.isAnnotationPresent(FileReference.class)) {
            return PersistentPropertyType.FILE;
        }

        if (isOfDateType(persistentProperty)) {
            return DATE;
        }

        if (isOfTimeType(persistentProperty)) {
            return TIME;
        }

        if (isOfDateTimeType(persistentProperty)) {
            return DATE_TIME;
        }

        return forType(attrType);
    }

    private static boolean isOfTimeType(PersistentProperty persistentProperty) {
        Class attrType = persistentProperty.getType();

        if (java.sql.Time.class.equals(attrType)) {
            return true;
        }

        if (LocalTime.class.equals(attrType)) {
            return true;
        }

        if (Date.class.equals(attrType) || Calendar.class.equals(attrType)) {
            return hasTemporalType(persistentProperty, TemporalType.TIME);
        }

        return false;
    }

    private static boolean isOfDateType(PersistentProperty persistentProperty) {
        Class<?> attrType = persistentProperty.getType();

        if (java.sql.Date.class.equals(attrType)) {
            return true;
        }

        if (LocalDate.class.equals(attrType)) {
            return true;
        }

        if (Date.class.equals(attrType) || Calendar.class.equals(attrType)) {
            return hasTemporalType(persistentProperty, TemporalType.DATE);
        }

        return false;
    }

    private static boolean isOfDateTimeType(PersistentProperty persistentProperty) {
        Class<?> attrType = persistentProperty.getType();

        if (Timestamp.class.equals(attrType)) {
            return true;
        }

        if (DateTime.class.equals(attrType) || LocalDateTime.class.equals(attrType)) {
            return true;
        }

        if (Date.class.equals(attrType) || Calendar.class.equals(attrType)) {
            return hasTemporalType(persistentProperty, TemporalType.TIMESTAMP);
        }

        return false;
    }

    public static boolean isSupportedAttributeType(PersistentPropertyType attributeType) {
        return attributeType != UNKNOWN;
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

    private static PersistentPropertyType forType(Class<?> attrType) {
        if (Boolean.class.equals(attrType) || boolean.class.equals(attrType)) {
            return BOOL;
        }

        if (isNumberInteger(attrType)) {
            return NUMBER_INTEGER;
        }

        if (isNumberFloat(attrType)) {
            return NUMBER_FLOAT;
        }

        if (String.class.equals(attrType) || UUID.class.equals(attrType)) {
            return STRING;
        }

        if (byte[].class.equals(attrType)) {
            return FILE;
        }

        return UNKNOWN;
    }

    private static boolean hasTemporalType(PersistentProperty persistentProperty, TemporalType temporalType) {
        Temporal temporal = (Temporal) persistentProperty.findAnnotation(Temporal.class);
        return temporal != null && temporal.value() == temporalType;
    }
}