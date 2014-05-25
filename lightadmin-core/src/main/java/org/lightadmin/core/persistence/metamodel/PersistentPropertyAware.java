package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.mapping.PersistentProperty;

public interface PersistentPropertyAware {

    void setPersistentProperty(PersistentProperty persistentProperty);
}