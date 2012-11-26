package org.lightadmin.core.config.domain.show;

import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;

import java.util.Set;

public interface ShowViewConfigurationUnit extends ConfigurationUnit, Iterable<FieldMetadata>{

	Set<FieldMetadata> getFields();
}