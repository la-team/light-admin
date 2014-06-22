package org.lightadmin.core.config.domain.unit.handler;

import org.lightadmin.core.config.domain.field.FieldMetadata;

public interface FieldHandler<T extends FieldMetadata> {

    void doWithField(T field);
}