package org.lightadmin.core.persistence.metamodel;

import com.google.common.base.Predicate;
import org.springframework.data.rest.repository.AttributeMetadata;

import java.util.Collection;

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newLinkedList;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.isOfBinaryFileType;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.isOfFileReferenceType;

public abstract class AttributeMetadataUtils {

    public static Collection<AttributeMetadata> fileReferenceAttributeMetadatas(Collection<AttributeMetadata> attributeMetadatas) {
        return newLinkedList(filter(attributeMetadatas, new FileReferenceAttributePredicate()));
    }

    public static Collection<AttributeMetadata> binaryFileAttributeMetadatas(Collection<AttributeMetadata> attributeMetadatas) {
        return newLinkedList(filter(attributeMetadatas, new BinaryFileAttributePredicate()));
    }

    public static Collection<AttributeMetadata> fileAttributeMetadatas(Collection<AttributeMetadata> attributeMetadatas) {
        return newLinkedList(filter(attributeMetadatas,
                or(new BinaryFileAttributePredicate(), new FileReferenceAttributePredicate()))
        );
    }

    public static class FileReferenceAttributePredicate implements Predicate<AttributeMetadata> {
        @Override
        public boolean apply(AttributeMetadata attributeMetadata) {
            return isOfFileReferenceType(attributeMetadata);
        }
    }

    public static class BinaryFileAttributePredicate implements Predicate<AttributeMetadata> {
        @Override
        public boolean apply(AttributeMetadata attributeMetadata) {
            return isOfBinaryFileType(attributeMetadata);
        }
    }
}