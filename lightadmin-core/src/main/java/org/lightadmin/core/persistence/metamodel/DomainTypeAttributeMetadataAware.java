package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.mapping.PersistentProperty;

public interface DomainTypeAttributeMetadataAware {

    void setAttributeMetadata(PersistentProperty attributeMetadata);
}