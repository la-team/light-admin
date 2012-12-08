package org.lightadmin.core.rest;

import org.lightadmin.core.config.domain.field.FieldMetadata;

import java.util.Set;

public class DomainTypeResource {

	private final Object resource;
	private final Set<FieldMetadata> fieldMetadatas;

	public DomainTypeResource( final Object resource, final Set<FieldMetadata> fieldMetadatas ) {
		this.resource = resource;
		this.fieldMetadatas = fieldMetadatas;
	}

	public Object getResource() {
		return resource;
	}

	public Set<FieldMetadata> getFieldMetadatas() {
		return fieldMetadatas;
	}
}