package org.lightadmin.api.config.unit;

import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

import java.util.Set;

public interface FieldSetConfigurationUnit extends ConfigurationUnit, Iterable<FieldMetadata> {

    Set<FieldMetadata> getFields();

    FieldMetadata getField(String fieldName);

    void addField(FieldMetadata fieldMetadata);

    boolean isEmpty();

}
