package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainTypeBasicConfiguration {

    String getConfigurationName();

    Class<?> getDomainType();

    String getDomainTypeName();

    DomainTypeEntityMetadata getDomainTypeEntityMetadata();

    JpaRepository<?, ?> getRepository();

    EntityMetadataConfigurationUnit getEntityConfiguration();

}
