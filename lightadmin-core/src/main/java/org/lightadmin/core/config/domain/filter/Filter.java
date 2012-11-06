package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

import java.io.Serializable;

// TODO: max: Please add XXXMetadata
public interface Filter extends Serializable {

	String getFieldName();

	void setAttributeMetadata( DomainTypeAttributeMetadata attributeMetadata );

	DomainTypeAttributeMetadata getAttributeMetadata();

}