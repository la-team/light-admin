package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class NonManagedDomainTypeConfiguration implements DomainTypeBasicConfiguration {

    private final EntityMetadataConfigurationUnit entityConfiguration;

    private final JpaRepository<?, ? extends Serializable> repository;
    private final PersistentEntity persistentEntity;

    public NonManagedDomainTypeConfiguration(EntityMetadataConfigurationUnit entityConfiguration, PersistentEntity persistentEntity, JpaRepository<?, ? extends Serializable> repository) {
        this.entityConfiguration = entityConfiguration;
        this.persistentEntity = persistentEntity;
        this.repository = repository;
    }

    @Override
    public String getConfigurationName() {
        return persistentEntity.getType().getSimpleName();
    }

    @Override
    public Class<?> getDomainType() {
        return persistentEntity.getType();
    }

    @Override
    public String getDomainTypeName() {
        return StringUtils.uncapitalize(persistentEntity.getName());
    }

    @Override
    public PersistentEntity getPersistentEntity() {
        return persistentEntity;
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
