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
package org.lightadmin.core.config.domain.configuration.support;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import java.io.Serializable;

public abstract class EntityNameExtractorFactory {

    public static EntityNameExtractor<?> forPersistentEntity(PersistentEntity persistentEntity) {
        return new PersistentEntityNameExtractor(persistentEntity);
    }

    public static EntityNameExtractor<?> forPersistentEntity(String entityName, PersistentEntity persistentEntity) {
        return new PersistentEntityNameExtractor(entityName, persistentEntity);
    }

    public static EntityNameExtractor<?> forNamedPersistentEntity(String name) {
        return new NamedPersistentEntityNameExtractor(name);
    }

    private static class PersistentEntityNameExtractor implements EntityNameExtractor<Object> {

        private final PersistentEntity persistentEntity;
        private final String entityName;

        public PersistentEntityNameExtractor(PersistentEntity persistentEntity) {
            this(persistentEntity.getType().getSimpleName(), persistentEntity);
        }

        public PersistentEntityNameExtractor(String entityName, PersistentEntity persistentEntity) {
            this.persistentEntity = persistentEntity;
            this.entityName = entityName;
        }

        @Override
        public String apply(final Object entity) {
            BeanWrapper beanWrapper = new DirectFieldAccessFallbackBeanWrapper(entity);
            Object entityId = beanWrapper.getPropertyValue(persistentEntity.getIdProperty().getName());

            return String.format("%s #%s", entityName, entityId);
        }
    }

    private static class NamedPersistentEntityNameExtractor implements EntityNameExtractor<Object>, Serializable {

        private final String nameField;

        private NamedPersistentEntityNameExtractor(final String nameField) {
            this.nameField = nameField;
        }

        @Override
        public String apply(final Object input) {
            return PropertyAccessorFactory.forBeanPropertyAccess(input).getPropertyValue(nameField).toString();
        }
    }
}