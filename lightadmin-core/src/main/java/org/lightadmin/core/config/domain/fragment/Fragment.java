package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.beans.parsing.ConfigurationUnitPropertyFilter;

import java.util.Set;

public interface Fragment {

	Fragment filter( ConfigurationUnitPropertyFilter propertyFilter );

	Set<FieldMetadata> getFields();

	FieldMetadata getField( String fieldName );
}