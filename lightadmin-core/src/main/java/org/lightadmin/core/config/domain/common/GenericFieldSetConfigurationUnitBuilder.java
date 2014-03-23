package org.lightadmin.core.config.domain.common;

import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;

import static org.lightadmin.core.config.domain.field.FieldMetadataFactory.customField;
import static org.lightadmin.core.config.domain.field.FieldMetadataFactory.transientField;

public class GenericFieldSetConfigurationUnitBuilder extends AbstractFieldSetConfigurationBuilder<FieldSetConfigurationUnit, FieldSetConfigurationUnitBuilder>
        implements FieldSetConfigurationUnitBuilder {

    private final FieldSetConfigurationUnit configurationUnit;

    public GenericFieldSetConfigurationUnitBuilder(Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        super(domainType);

        this.configurationUnit = new DefaultFieldSetConfigurationUnit(domainType, configurationUnitType);
    }

    @Override
    public FieldSetConfigurationUnitBuilder dynamic(final String expression) {
        addCurrentFieldToUnit();

        currentFieldMetadata = transientField("Undefined", expression);

        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FieldSetConfigurationUnitBuilder renderable(final FieldValueRenderer renderer) {
        addCurrentFieldToUnit();

        currentFieldMetadata = customField("Undefined", renderer);

        return this;
    }

    @Override
    public FieldSetConfigurationUnit build() {
        addCurrentFieldToUnit();

        return configurationUnit;
    }


    @Override
    protected void addCurrentFieldToUnit() {
        if (currentFieldMetadata != null) {
            configurationUnit.addField(currentFieldMetadata);
        }
    }
}
