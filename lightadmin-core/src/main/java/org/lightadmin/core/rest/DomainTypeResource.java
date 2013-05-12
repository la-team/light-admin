package org.lightadmin.core.rest;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.field.FieldMetadata;

import java.util.Set;

public class DomainTypeResource {

	private final Object resource;
	private final DomainConfigurationUnitType configurationUnitType;
	private final Set<FieldMetadata> fieldMetadatas;

	public DomainTypeResource( final Object resource, final DomainConfigurationUnitType configurationUnitType, final Set<FieldMetadata> fieldMetadatas ) {
		this.resource = resource;
		this.configurationUnitType = configurationUnitType;
		this.fieldMetadatas = fieldMetadatas;
	}

	public Object getResource() {
		return resource;
	}

	public Set<FieldMetadata> getFieldMetadatas() {
		return fieldMetadatas;
	}

	public DomainConfigurationUnitType getConfigurationUnitType() {
		return configurationUnitType;
	}
}