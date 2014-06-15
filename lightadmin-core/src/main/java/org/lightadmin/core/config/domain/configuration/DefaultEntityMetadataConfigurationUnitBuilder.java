package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.api.config.builder.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.common.AbstractFieldSetConfigurationBuilder;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

import static org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory.forNamedPersistentEntity;

public class DefaultEntityMetadataConfigurationUnitBuilder extends AbstractFieldSetConfigurationBuilder<EntityMetadataConfigurationUnit, EntityMetadataConfigurationUnitBuilder> implements EntityMetadataConfigurationUnitBuilder {

    private final DefaultEntityMetadataConfigurationUnit configurationUnit;

    public DefaultEntityMetadataConfigurationUnitBuilder(Class<?> domainType) {
        super(domainType);
        configurationUnit = new DefaultEntityMetadataConfigurationUnit(domainType);
        configurationUnit.setSingularName(domainType.getSimpleName());
        configurationUnit.setPluralName(domainType.getSimpleName());
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder nameField(final String nameField) {
        configurationUnit.setNameExtractor(forNamedPersistentEntity(nameField));
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder nameExtractor(final EntityNameExtractor<?> nameExtractor) {
        configurationUnit.setNameExtractor(nameExtractor);
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder singularName(final String singularName) {
        configurationUnit.setSingularName(singularName);
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder pluralName(final String pluralName) {
        configurationUnit.setPluralName(pluralName);
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder repositoryEventListener(Class<? extends AbstractRepositoryEventListener> listenerClass) {
        configurationUnit.setRepositoryEventListener(listenerClass);
        return this;
    }

    @Override
    protected void addCurrentFieldToUnit() {
        if (currentFieldMetadata != null) {
            configurationUnit.addField(currentFieldMetadata);
        }
    }

    @Override
    public EntityMetadataConfigurationUnit build() {
        addCurrentFieldToUnit();
        return configurationUnit;
    }
}
