package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.common.GenericFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.metamodel.PersistentPropertyType;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.ClassUtils;

import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.CONFIGURATION;
import static org.lightadmin.core.persistence.metamodel.PersistentPropertyType.isSupportedAttributeType;

public class EmptyConfigurationUnitPostProcessor extends EntityMetadataResolverAwareConfigurationUnitPostProcessor {

    public EmptyConfigurationUnitPostProcessor(final MappingContext mappingContext) {
        super(mappingContext);
    }

    @Override
    public ConfigurationUnit postProcess(final ConfigurationUnit configurationUnit, ConfigurationUnits configurationUnits) {
        if (configurationUnit.getDomainConfigurationUnitType() != CONFIGURATION && isEmptyFieldSetConfigurationUnit(configurationUnit)) {
            return fieldSetUnitWithPersistentFields(configurationUnit.getDomainType(), configurationUnit.getDomainConfigurationUnitType());
        }

        return configurationUnit;
    }

    private FieldSetConfigurationUnit fieldSetUnitWithPersistentFields(final Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        final FieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder = new GenericFieldSetConfigurationUnitBuilder(domainType, configurationUnitType);

        PersistentEntity persistentEntity = resolveEntityMetadata(domainType);

        persistentEntity.doWithProperties(new SimplePropertyHandler() {
            @Override
            public void doWithPersistentProperty(PersistentProperty<?> property) {
                PersistentPropertyType persistentPropertyType = PersistentPropertyType.forPersistentProperty(property);
                if (isSupportedAttributeType(persistentPropertyType)) {
                    fieldSetConfigurationUnitBuilder.field(property.getName());
                }
            }
        });

        return fieldSetConfigurationUnitBuilder.build();
    }

    private boolean isEmptyFieldSetConfigurationUnit(ConfigurationUnit configurationUnit) {
        return ClassUtils.isAssignableValue(FieldSetConfigurationUnit.class, configurationUnit) && ((FieldSetConfigurationUnit) configurationUnit).isEmpty();
    }
}
