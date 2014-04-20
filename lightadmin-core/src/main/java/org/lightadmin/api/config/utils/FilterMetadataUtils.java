package org.lightadmin.api.config.utils;

import org.lightadmin.api.config.builder.FilterMetadataBuilder;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadataFactory;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.filter.FilterMetadata;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.util.Assert;

public abstract class FilterMetadataUtils {

    private FilterMetadataUtils() {
    }

    public static FilterMetadata filter(String filterName, String fieldName) {
        return new DefaultFilterMetadata(filterName, fieldName);
    }

    public static FilterMetadataBuilder filter() {
        return new DefaultFilterMetadataBuilder();
    }

    private static class DefaultFilterMetadata implements FilterMetadata {

        private final PersistentFieldMetadata fieldMetadata;

        public DefaultFilterMetadata(final String filterName, final String fieldName) {
            Assert.notNull(filterName);
            Assert.notNull(fieldName);

            this.fieldMetadata = (PersistentFieldMetadata) FieldMetadataFactory.persistentField(filterName, fieldName);
        }

        @Override
        public String getName() {
            return fieldMetadata.getName();
        }

        @Override
        public void setName(final String name) {
            fieldMetadata.setName(name);
        }

        @Override
        public String getFieldName() {
            return fieldMetadata.getField();
        }

        @Override
        public String getUuid() {
            return fieldMetadata.getUuid();
        }

        @Override
        public Class<?> getType() {
            return fieldMetadata.getPersistentProperty().getType();
        }

        @Override
        public FieldMetadata getFieldMetadata() {
            return fieldMetadata;
        }

        @Override
        public void setAttributeMetadata(final PersistentProperty attributeMetadata) {
            this.fieldMetadata.setAttributeMetadata(attributeMetadata);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final DefaultFilterMetadata that = (DefaultFilterMetadata) o;

            return fieldMetadata.equals(that.fieldMetadata);
        }

        @Override
        public int hashCode() {
            return fieldMetadata.hashCode();
        }
    }

    private static class DefaultFilterMetadataBuilder implements FilterMetadataBuilder {

        private String filterName = "Undefined";
        private String fieldName = "Undefined";

        @Override
        public DefaultFilterMetadataBuilder field(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        @Override
        public DefaultFilterMetadataBuilder caption(String filterName) {
            this.filterName = filterName;
            return this;
        }

        @Override
        public FilterMetadata build() {
            return filter(filterName, fieldName);
        }
    }
}