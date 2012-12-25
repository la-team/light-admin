package org.lightadmin.core.config.domain.unit;

import java.util.Set;

import org.lightadmin.core.config.domain.field.FieldMetadata;

public interface FieldSetConfigurationUnit extends ConfigurationUnit, Iterable<FieldMetadata>{

	Set<FieldMetadata> getFields();

	void addField( FieldMetadata fieldMetadata );

}
