package org.lightadmin.core.config.domain.common;

import org.lightadmin.api.config.builder.PersistentFieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.utils.Editors;
import org.lightadmin.api.config.utils.EnumElement;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.lightadmin.core.view.editor.JspFragmentFieldControl;

import static java.util.Arrays.asList;

public class PersistentFieldSetConfigurationUnitBuilderAdapter implements PersistentFieldSetConfigurationUnitBuilder {

    private final GenericFieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder;

    public PersistentFieldSetConfigurationUnitBuilderAdapter(Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        this.fieldSetConfigurationUnitBuilder = new GenericFieldSetConfigurationUnitBuilder(domainType, configurationUnitType);
    }

    @Override
    public PersistentFieldSetConfigurationUnitBuilder field(final String fieldName) {
        fieldSetConfigurationUnitBuilder.field(fieldName);
        return this;
    }

    @Override
    public PersistentFieldSetConfigurationUnitBuilder caption(final String caption) {
        fieldSetConfigurationUnitBuilder.caption(caption);
        return this;
    }

    @Deprecated
    @Override
    public PersistentFieldSetConfigurationUnitBuilder enumeration(String... values) {
        fieldSetConfigurationUnitBuilder.withCustomControl(Editors.enumeration(values));
        return this;
    }

    @Override
    public PersistentFieldSetConfigurationUnitBuilder enumeration(EnumElement... elements) {
        fieldSetConfigurationUnitBuilder.enumeration(elements);
        fieldSetConfigurationUnitBuilder.withCustomControl(Editors.enumeration(asList(elements)));
        return this;
    }

    @Override
    public PersistentFieldSetConfigurationUnitBuilder editor(JspFragmentFieldControl editControl) {
        fieldSetConfigurationUnitBuilder.withCustomControl(editControl);
        return this;
    }

    @Override
    public FieldSetConfigurationUnit build() {
        return fieldSetConfigurationUnitBuilder.build();
    }
}
