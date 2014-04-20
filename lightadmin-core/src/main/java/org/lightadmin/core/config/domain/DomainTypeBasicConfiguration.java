package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mapping.PersistentEntity;

public interface DomainTypeBasicConfiguration {

    String getConfigurationName();

    Class<?> getDomainType();

    String getDomainTypeName();

    PersistentEntity getPersistentEntity();

    JpaRepository<?, ?> getRepository();

    EntityMetadataConfigurationUnit getEntityConfiguration();

}
