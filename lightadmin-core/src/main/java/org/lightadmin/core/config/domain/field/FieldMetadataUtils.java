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
package org.lightadmin.core.config.domain.field;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.StringUtils;
import org.lightadmin.core.config.domain.filter.FilterMetadata;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;
import static org.springframework.util.ClassUtils.isAssignableValue;

public class FieldMetadataUtils {

    public static Set<FieldMetadata> extractFields(Iterable<FilterMetadata> filterMetadatas) {
        return newLinkedHashSet(Collections2.transform(newLinkedHashSet(filterMetadatas), new FieldMetadataExtractor()));
    }

    public static Predicate<FieldMetadata> persistentFieldMetadataPredicate() {
        return new FieldMetadataTypePredicate(PersistentFieldMetadata.class);
    }

    public static Predicate<FieldMetadata> transientFieldMetadataPredicate() {
        return new FieldMetadataTypePredicate(TransientFieldMetadata.class);
    }

    public static Predicate<FieldMetadata> customFieldMetadataPredicate() {
        return new FieldMetadataTypePredicate(CustomFieldMetadata.class);
    }

    public static Set<FieldMetadata> selectFields(Set<FieldMetadata> fieldMetadatas, Predicate<FieldMetadata>... predicates) {
        return newLinkedHashSet(Collections2.filter(fieldMetadatas, Predicates.and(predicates)));
    }

    public static Set<FieldMetadata> persistentFields(Set<FieldMetadata> fieldMetadatas) {
        return newLinkedHashSet(Collections2.filter(fieldMetadatas, persistentFieldMetadataPredicate()));
    }

    public static Set<FieldMetadata> transientFields(Set<FieldMetadata> fieldMetadatas) {
        return newLinkedHashSet(Collections2.filter(fieldMetadatas, transientFieldMetadataPredicate()));
    }

    public static Set<FieldMetadata> customFields(Set<FieldMetadata> fieldMetadatas) {
        return newLinkedHashSet(Collections2.filter(fieldMetadatas, customFieldMetadataPredicate()));
    }

    public static FieldMetadata primaryKeyPersistentField(Set<FieldMetadata> fields) {
        for (FieldMetadata field : persistentFields(fields)) {
            PersistentFieldMetadata persistentFieldMetadata = (PersistentFieldMetadata) field;
            if (persistentFieldMetadata.isPrimaryKey()) {
                return persistentFieldMetadata;
            }
        }
        return null;
    }

    public static PersistentFieldMetadata getPersistentField(final Set<FieldMetadata> fields, String fieldName) {
        for (FieldMetadata field : persistentFields(fields)) {
            PersistentFieldMetadata persistentFieldMetadata = (PersistentFieldMetadata) field;
            if (StringUtils.equals(persistentFieldMetadata.getField(), fieldName)) {
                return persistentFieldMetadata;
            }
        }
        return null;
    }

    public static class FieldMetadataComparator implements Comparator<FieldMetadata>, Serializable {

        @Override
        public int compare(final FieldMetadata fieldMetadata, final FieldMetadata fieldMetadata2) {
            if (isPrimaryKey(fieldMetadata)) {
                return -1;
            }

            if (isPrimaryKey(fieldMetadata2)) {
                return 1;
            }

            if (fieldMetadata.getSortOrder() < fieldMetadata2.getSortOrder()) {
                return -1;
            }
            if (fieldMetadata.getSortOrder() > fieldMetadata2.getSortOrder()) {
                return 1;
            }

            return (fieldMetadata.equals(fieldMetadata2) ? 0 : 1);
        }

        private boolean isPrimaryKey(FieldMetadata fieldMetadata) {
            return isAssignableValue(PersistentFieldMetadata.class, fieldMetadata) && ((PersistentFieldMetadata) fieldMetadata).isPrimaryKey();
        }
    }

    public static class NameableComparator implements Comparator<Nameable> {

        @Override
        public int compare(final Nameable nameable, final Nameable nameable2) {
            return nameable.getName().compareTo(nameable2.getName());
        }
    }

    private static class FieldMetadataExtractor implements Function<FilterMetadata, FieldMetadata> {

        @Override
        public FieldMetadata apply(final FilterMetadata filterMetadata) {
            return filterMetadata.getFieldMetadata();
        }
    }

    private static class FieldMetadataTypePredicate implements Predicate<FieldMetadata> {

        private final Class<? extends FieldMetadata> fieldMetadataClass;

        private FieldMetadataTypePredicate(final Class<? extends FieldMetadata> fieldMetadataClass) {
            this.fieldMetadataClass = fieldMetadataClass;
        }

        @Override
        public boolean apply(final FieldMetadata fieldMetadata) {
            return isAssignableValue(fieldMetadataClass, fieldMetadata);
        }
    }
}
