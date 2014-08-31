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
        public PersistentProperty getAttributeMetadata() {
            return fieldMetadata.getPersistentProperty();
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
        public void setPersistentProperty(final PersistentProperty persistentProperty) {
            this.fieldMetadata.setPersistentProperty(persistentProperty);
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