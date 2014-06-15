package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.api.config.utils.EntityNameExtractor;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainConfigurationUnitType;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

public class DefaultEntityMetadataConfigurationUnit extends DefaultFieldSetConfigurationUnit implements EntityMetadataConfigurationUnit {

    private EntityNameExtractor<?> nameExtractor;
    private Class<? extends AbstractRepositoryEventListener> repositoryEventListener;

    private String singularName;
    private String pluralName;

    DefaultEntityMetadataConfigurationUnit(Class<?> domainType) {
        super(domainType, DomainConfigurationUnitType.CONFIGURATION);
    }

    @Override
    public Class<? extends AbstractRepositoryEventListener> getRepositoryEventListener() {
        return repositoryEventListener;
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

    public void setRepositoryEventListener(Class<? extends AbstractRepositoryEventListener> repositoryEventListener) {
        this.repositoryEventListener = repositoryEventListener;
    }

    @Override
    public DomainConfigurationUnitType getParentUnitType() {
        return null;
    }

}