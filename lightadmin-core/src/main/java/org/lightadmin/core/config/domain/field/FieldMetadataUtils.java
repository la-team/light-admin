package org.lightadmin.core.config.domain.field;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import org.apache.commons.lang.StringUtils;
import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.springframework.util.ClassUtils;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

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

    public static Set<FieldMetadata> addPrimaryKeyPersistentField(final Set<FieldMetadata> fields, final DomainTypeAttributeMetadata idAttribute) {
        final Set<FieldMetadata> fieldsWithPrimaryKey = newLinkedHashSet();
        PersistentFieldMetadata idField = new PersistentFieldMetadata(StringUtils.capitalize(idAttribute.getName()), idAttribute.getName(), true);
        idField.setAttributeMetadata(idAttribute);
        fieldsWithPrimaryKey.add(idField);
        fieldsWithPrimaryKey.addAll(fields);
        return fieldsWithPrimaryKey;
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

    public static boolean containsPersistentField(final Set<FieldMetadata> fields, String fieldName) {
        return getPersistentField(fields, fieldName) != null;
    }

    public static class FieldMetadataComparator implements Comparator<FieldMetadata> {

        @Override
        public int compare(final FieldMetadata fieldMetadata, final FieldMetadata fieldMetadata2) {
            if (fieldMetadata.getSortOrder() < fieldMetadata2.getSortOrder()) {
                return -1;
            }
            if (fieldMetadata.getSortOrder() > fieldMetadata2.getSortOrder()) {
                return 1;
            }
            return 0;
        }
    }

    public static class NameableComparator implements Comparator<Nameable> {

        @Override
        public int compare(final Nameable nameable, final Nameable nameable2) {
            return nameable.getName().compareTo(nameable2.getName());
        }
    }

    public static class DomainTypeAttributeMetadataComparator implements Comparator<DomainTypeAttributeMetadata> {

        @Override
        public int compare(final DomainTypeAttributeMetadata domainTypeAttributeMetadata, final DomainTypeAttributeMetadata domainTypeAttributeMetadata2) {
            return domainTypeAttributeMetadata.getName().compareTo(domainTypeAttributeMetadata2.getName());
        }
    }

    private static class FieldMetadataExtractor implements Function<FilterMetadata, FieldMetadata> {

        @Override
        public FieldMetadata apply(@Nullable final FilterMetadata filterMetadata) {
            return filterMetadata.getFieldMetadata();
        }
    }

    private static class FieldMetadataTypePredicate implements Predicate<FieldMetadata> {

        private final Class<? extends FieldMetadata> fieldMetadataClass;

        private FieldMetadataTypePredicate(final Class<? extends FieldMetadata> fieldMetadataClass) {
            this.fieldMetadataClass = fieldMetadataClass;
        }

        @Override
        public boolean apply(@Nullable final FieldMetadata fieldMetadata) {
            return ClassUtils.isAssignableValue(fieldMetadataClass, fieldMetadata);
        }
    }
}
