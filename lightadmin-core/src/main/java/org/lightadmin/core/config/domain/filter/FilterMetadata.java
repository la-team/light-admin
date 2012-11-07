package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;

public interface FilterMetadata extends DomainTypeAttributeMetadataAware {

	String getFieldName();
}