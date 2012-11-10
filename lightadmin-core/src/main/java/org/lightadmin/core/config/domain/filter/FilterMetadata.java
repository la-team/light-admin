package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;

import java.io.Serializable;

public interface FilterMetadata extends DomainTypeAttributeMetadataAware, Serializable {

	String getFieldName();
}