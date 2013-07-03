package org.lightadmin.core.config.domain.common;

import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.view.editor.JspFragmentFieldControl;

public interface PersistentFieldSetConfigurationUnitBuilder extends ConfigurationUnitBuilder<FieldSetConfigurationUnit> {

    PersistentFieldSetConfigurationUnitBuilder field(String fieldName);

    PersistentFieldSetConfigurationUnitBuilder caption(String caption);

    PersistentFieldSetConfigurationUnitBuilder enumeration(String... values);

    PersistentFieldSetConfigurationUnitBuilder enumeration(EnumElement... elements);

    PersistentFieldSetConfigurationUnitBuilder editor(JspFragmentFieldControl editControl);
}
