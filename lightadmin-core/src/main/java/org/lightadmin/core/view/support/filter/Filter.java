package org.lightadmin.core.view.support.filter;

import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;

public interface Filter {

	String getFieldName();

	void setAttributeMetadata( DomainTypeAttributeMetadata attributeMetadata );

	DomainTypeAttributeMetadata getAttributeMetadata();

}