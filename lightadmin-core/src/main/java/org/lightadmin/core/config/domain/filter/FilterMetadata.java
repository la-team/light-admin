package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.Identifiable;
import org.lightadmin.core.config.domain.field.Nameable;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyAware;
import org.springframework.data.mapping.PersistentProperty;

import java.io.Serializable;

public interface FilterMetadata extends PersistentPropertyAware, Identifiable, Nameable, Serializable {

    Class<?> getType();

    String getFieldName();

    PersistentProperty getAttributeMetadata();

    FieldMetadata getFieldMetadata();
}