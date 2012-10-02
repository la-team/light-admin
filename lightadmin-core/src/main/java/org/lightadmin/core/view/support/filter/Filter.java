package org.lightadmin.core.view.support.filter;

import org.springframework.data.rest.repository.jpa.JpaAttributeMetadata;

public interface Filter {

	String getFieldName();

	void setAttributeMetadata( JpaAttributeMetadata attributeMetadata );

	JpaAttributeMetadata getAttributeMetadata();

}