package org.lightadmin.core.config.domain.fragment;

import java.io.Serializable;
import java.util.Set;

public interface Fragment extends Serializable {

	Set<FieldMetadata> getFields();

	FieldMetadata getField( String fieldName );
}