package org.lightadmin.api.config.unit;

import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.handler.FieldHandler;

import java.util.Set;

public interface FieldSetConfigurationUnit extends ConfigurationUnit, Iterable<FieldMetadata> {

    Set<FieldMetadata> getFields();

    void addField(FieldMetadata fieldMetadata);

    boolean isEmpty();

    void doWithFields(FieldHandler<FieldMetadata> handler);

    void doWithPersistentFields(FieldHandler<PersistentFieldMetadata> handler);

}