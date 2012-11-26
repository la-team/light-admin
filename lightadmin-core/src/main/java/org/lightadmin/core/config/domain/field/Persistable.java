package org.lightadmin.core.config.domain.field;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

public interface Persistable {

	DomainTypeAttributeMetadata getAttributeMetadata();

	String getField();

	boolean isPrimaryKey();
}