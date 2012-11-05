package org.lightadmin.core.config.domain.fragment;

import java.util.Set;

public interface Fragment {

	Set<FieldMetadata> getFields();

	FieldMetadata getField( String fieldName );
}