package org.lightadmin.core.config.domain.field;

import org.springframework.data.mapping.PersistentProperty;

public interface Persistable {

    PersistentProperty getPersistentProperty();

    String getField();

    boolean isPrimaryKey();

    boolean isRequired();

    boolean isGeneratedValue();
}