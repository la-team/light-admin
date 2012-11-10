package org.lightadmin.core.config.domain.fragment.field;

import java.util.UUID;

public interface FieldMetadata extends Orderable {

	String getName();

	boolean isSortable();

	UUID getUuid();
}