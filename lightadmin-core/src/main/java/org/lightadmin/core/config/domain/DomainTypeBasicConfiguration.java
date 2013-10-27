package org.lightadmin.core.config.domain;

import org.lightadmin.api.config.unit.EntityMetadataConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.repository.DynamicJpaRepository;

public interface DomainTypeBasicConfiguration {

    String getConfigurationName();

    Class<?> getDomainType();

    String getDomainTypeName();

    DomainTypeEntityMetadata getDomainTypeEntityMetadata();

    DynamicJpaRepository<?, ?> getRepository();

    EntityMetadataConfigurationUnit getEntityConfiguration();

}
