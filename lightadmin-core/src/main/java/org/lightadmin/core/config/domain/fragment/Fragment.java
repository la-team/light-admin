package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.field.FieldMetadata;

import java.io.Serializable;
import java.util.Set;

public interface Fragment extends Serializable {

	Set<FieldMetadata> getFields();
}