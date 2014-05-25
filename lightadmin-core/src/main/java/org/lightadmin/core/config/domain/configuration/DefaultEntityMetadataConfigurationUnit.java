package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.PersistentEntityAware;
import org.springframework.data.mapping.PersistentEntity;

public class DefaultEntityMetadataConfigurationUnit extends DefaultFieldSetConfigurationUnit
        implements EntityMetadataConfigurationUnit, PersistentEntityAware {

    private EntityNameExtractor<?> nameExtractor;

    private String singularName;
    private String pluralName;

    DefaultEntityMetadataConfigurationUnit(Class<?> domainType) {
        super(domainType, DomainConfigurationUnitType.CONFIGURATION);
    }

    @Override
    public EntityNameExtractor getNameExtractor() {
        return nameExtractor;
    }

    @Override
    public String getSingularName() {
        return this.singularName;
    }

    @Override
    public String getPluralName() {
        return this.pluralName;
    }

    public void setNameExtractor(EntityNameExtractor<?> nameExtractor) {
        this.nameExtractor = nameExtractor;
    }

    public void setSingularName(String singularName) {
        this.singularName = singularName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    @Override
    public DomainConfigurationUnitType getParentUnitType() {
        return null;
    }

    @Override
    public void setPersistentEntity(final PersistentEntity persistenEntity) {
        if (nameExtractor == null) {
            this.nameExtractor = EntityNameExtractorFactory.forPersistentEntity(this.singularName, persistenEntity);
        }
    }
}
