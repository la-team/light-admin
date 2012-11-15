package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.Identifiable;
import org.lightadmin.core.config.domain.field.Nameable;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadataAware;

import java.io.Serializable;

public interface FilterMetadata extends DomainTypeAttributeMetadataAware, Identifiable, Nameable, Serializable {

	Class<?> getType();

	String getFieldName();

	DomainTypeAttributeMetadata getAttributeMetadata();

	FieldMetadata getFieldMetadata();
}