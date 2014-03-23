package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class NonManagedDomainTypeConfiguration implements DomainTypeBasicConfiguration {

    private final EntityMetadataConfigurationUnit entityConfiguration;
    private final DomainTypeEntityMetadata entityMetadata;
    private final JpaRepository<?, ? extends Serializable> repository;

    public NonManagedDomainTypeConfiguration(EntityMetadataConfigurationUnit entityConfiguration, DomainTypeEntityMetadata entityMetadata, JpaRepository<?, ? extends Serializable> repository) {
        this.entityConfiguration = entityConfiguration;
        this.entityMetadata = entityMetadata;
        this.repository = repository;
    }

    @Override
    public String getConfigurationName() {
        return entityConfiguration.getDomainType().getSimpleName();
    }

    @Override
    public Class<?> getDomainType() {
        return entityConfiguration.getDomainType();
    }

    @Override
    public String getDomainTypeName() {
        return StringUtils.uncapitalize(entityMetadata.getEntityName());
    }

    @Override
    public DomainTypeEntityMetadata getDomainTypeEntityMetadata() {
        return entityMetadata;
    }

    @Override
    public JpaRepository<?, ?> getRepository() {
        return repository;
    }

    @Override
    public EntityMetadataConfigurationUnit getEntityConfiguration() {
        return entityConfiguration;
    }

}
