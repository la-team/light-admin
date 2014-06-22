package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.util.Assert;

import java.io.Serializable;

import static org.springframework.util.StringUtils.uncapitalize;

public class NonManagedDomainTypeConfiguration implements DomainTypeBasicConfiguration {

    private static RelProvider REL_PROVIDER = new EvoInflectorRelProvider();

    private final EntityMetadataConfigurationUnit entityConfiguration;

    private final JpaRepository<?, ? extends Serializable> repository;
    private final PersistentEntity persistentEntity;

    public NonManagedDomainTypeConfiguration(EntityMetadataConfigurationUnit entityConfiguration, PersistentEntity persistentEntity, JpaRepository<?, ? extends Serializable> repository) {
        Assert.notNull(persistentEntity, "Persistent Entity must not be null!");
        Assert.notNull(repository, "Repository must not be null!");
        Assert.notNull(repository, "Entity Configuration must not be null!");

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
        return uncapitalize(getDomainType().getSimpleName());
    }

    @Override
    public String getPluralDomainTypeName() {
        return REL_PROVIDER.getCollectionResourceRelFor(getDomainType());
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
