package org.lightadmin.core.view.support.filter;

import org.springframework.data.rest.repository.AttributeMetadata;

public interface Filter {

	String getFieldName();

	void setAttributeMetadata( AttributeMetadata attributeMetadata );

	AttributeMetadata getAttributeMetadata();

}