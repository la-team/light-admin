package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.api.config.builder.EntityMetadataConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

public class DefaultEntityMetadataConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<EntityMetadataConfigurationUnit> implements EntityMetadataConfigurationUnitBuilder {

    private EntityNameExtractor<?> nameExtractor;

    private String singularName;
    private String pluralName;

    public DefaultEntityMetadataConfigurationUnitBuilder(Class<?> domainType) {
        super(domainType);
        this.singularName = domainType.getSimpleName();
        this.pluralName = domainType.getSimpleName();
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder nameField(final String nameField) {
        this.nameExtractor = EntityNameExtractorFactory.forNamedPersistentEntity(nameField);
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder nameExtractor(final EntityNameExtractor<?> nameExtractor) {
        this.nameExtractor = nameExtractor;
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder singularName(final String singularName) {
        this.singularName = singularName;
        return this;
    }

    @Override
    public EntityMetadataConfigurationUnitBuilder pluralName(final String pluralName) {
        this.pluralName = pluralName;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntityMetadataConfigurationUnit build() {
        return new DefaultEntityMetadataConfigurationUnit(getDomainType(), nameExtractor, singularName, pluralName);
    }
}