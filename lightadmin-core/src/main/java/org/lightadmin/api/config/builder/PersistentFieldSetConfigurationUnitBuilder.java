package org.lightadmin.api.config.builder;

import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitBuilder;
import org.lightadmin.core.view.editor.JspFragmentFieldControl;

public interface PersistentFieldSetConfigurationUnitBuilder extends ConfigurationUnitBuilder<FieldSetConfigurationUnit> {

    PersistentFieldSetConfigurationUnitBuilder field(String fieldName);

    PersistentFieldSetConfigurationUnitBuilder caption(String caption);

    @Deprecated
    PersistentFieldSetConfigurationUnitBuilder enumeration(String... values);

    PersistentFieldSetConfigurationUnitBuilder enumeration(EnumElement... elements);

    PersistentFieldSetConfigurationUnitBuilder editor(JspFragmentFieldControl editControl);
}
