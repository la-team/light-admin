package org.lightadmin.core.config.domain.configuration.support;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.BeanWrapper;

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
            BeanWrapper wrapper = BeanWrapper.create(entity, null);
            Object entityId = wrapper.getProperty(persistentEntity.getIdProperty());

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