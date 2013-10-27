package org.lightadmin.core.config.domain.configuration.support;

import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.Serializable;

public abstract class EntityNameExtractorFactory {

    public static EntityNameExtractor<?> forPersistentEntity(DomainTypeEntityMetadata entityMetadata) {
        return new PersistentEntityNameExtractor(entityMetadata);
    }

    public static EntityNameExtractor<?> forPersistentEntity(String entityName, DomainTypeEntityMetadata entityMetadata) {
        return new PersistentEntityNameExtractor(entityName, entityMetadata);
    }

    public static EntityNameExtractor<?> forNamedPersistentEntity(String name) {
        return new NamedPersistentEntityNameExtractor(name);
    }

    private static class PersistentEntityNameExtractor implements EntityNameExtractor<Object> {

        private final DomainTypeEntityMetadata entityMetadata;

        private final String entityName;

        public PersistentEntityNameExtractor(final DomainTypeEntityMetadata entityMetadata) {
            this.entityMetadata = entityMetadata;
            this.entityName = entityMetadata.getEntityName();
        }

        public PersistentEntityNameExtractor(String entityName, final DomainTypeEntityMetadata entityMetadata) {
            this.entityMetadata = entityMetadata;
            this.entityName = entityName;
        }

        @Override
        public String apply(final Object entity) {
            return String.format("%s #%s", entityName, entityMetadata.getIdAttribute().getValue(entity));
        }
    }

    private static class NamedPersistentEntityNameExtractor implements EntityNameExtractor<Object>, Serializable {

        private final String nameField;

        private NamedPersistentEntityNameExtractor(final String nameField) {
            this.nameField = nameField;
        }

        @Override
        public String apply(final Object input) {
            BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(input);
            return beanWrapper.getPropertyValue(nameField).toString();
        }
    }
}