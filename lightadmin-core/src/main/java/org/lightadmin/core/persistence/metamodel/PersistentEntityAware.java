package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.mapping.PersistentEntity;

public interface PersistentEntityAware {

    void setPersistentEntity(PersistentEntity persistenEntity);
}