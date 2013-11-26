package org.lightadmin.api.config.builder;

import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;

public interface FieldSetConfigurationUnitBuilder extends ConfigurationUnitBuilder<FieldSetConfigurationUnit> {

    FieldSetConfigurationUnitBuilder field(String fieldName);

    FieldSetConfigurationUnitBuilder caption(String caption);

    FieldSetConfigurationUnitBuilder dynamic(String expression);

    FieldSetConfigurationUnitBuilder renderable(FieldValueRenderer<?> renderer);

    FieldSetConfigurationUnitBuilder enumeration(EnumElement... elements);

}
