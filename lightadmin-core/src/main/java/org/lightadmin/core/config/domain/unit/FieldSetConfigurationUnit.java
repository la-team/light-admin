package org.lightadmin.core.config.domain.unit;

import org.lightadmin.core.config.domain.field.FieldMetadata;

import java.util.Set;

public interface FieldSetConfigurationUnit extends ConfigurationUnit, Iterable<FieldMetadata>{

	Set<FieldMetadata> getFields();

	void addField( FieldMetadata fieldMetadata );

	boolean isEmpty();

}
