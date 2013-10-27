package org.lightadmin.core.config.domain.common;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections15.functors.PrototypeFactory;
import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.api.config.utils.FieldValueRenderer;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.field.AbstractFieldMetadata;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

import javax.servlet.jsp.tagext.SimpleTag;

import static org.lightadmin.core.config.domain.field.FieldMetadataFactory.*;
import static org.springframework.util.StringUtils.capitalize;

public class GenericFieldSetConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<FieldSetConfigurationUnit> implements FieldSetConfigurationUnitBuilder {

    private final FieldSetConfigurationUnit configurationUnit;

    private FieldMetadata currentFieldMetadata;

    public GenericFieldSetConfigurationUnitBuilder(Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        super(domainType);

        this.configurationUnit = new DefaultFieldSetConfigurationUnit(domainType, configurationUnitType);
    }

    @Override
    public FieldSetConfigurationUnitBuilder field(final String fieldName) {
        addCurrentFieldToUnit();

        currentFieldMetadata = persistentField(capitalize(fieldName), fieldName);

        return this;
    }

    @Override
    public FieldSetConfigurationUnitBuilder caption(final String caption) {
        assertFieldMetadataIsNotNull();

        currentFieldMetadata.setName(caption);

        return this;
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

    FieldSetConfigurationUnitBuilder withCustomControl(SimpleTag customControl) {
        assertFieldMetadataType(currentFieldMetadata, AbstractFieldMetadata.class);

        try {
            BeanUtils.setProperty(customControl, "field", currentFieldMetadata.getUuid());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        AbstractFieldMetadata fieldMetadata = (AbstractFieldMetadata) currentFieldMetadata;
        fieldMetadata.setCustomControlFactory(PrototypeFactory.getInstance(customControl));

        return this;
    }

    @Override
    public FieldSetConfigurationUnit build() {
        addCurrentFieldToUnit();

        return configurationUnit;
    }

    private void addCurrentFieldToUnit() {
        if (currentFieldMetadata != null) {
            configurationUnit.addField(currentFieldMetadata);
        }
    }

    private void assertFieldMetadataIsNotNull() {
        if (currentFieldMetadata == null) {
            throw new RuntimeException("Field is not defined yet.");
        }
    }

    private static void assertFieldMetadataType(FieldMetadata currentFieldMetadata, Class<?> type) {
        if (currentFieldMetadata == null || !type.isAssignableFrom(currentFieldMetadata.getClass())) {
            throw new RuntimeException("Field is not defined or wrong type.");
        }
    }
}