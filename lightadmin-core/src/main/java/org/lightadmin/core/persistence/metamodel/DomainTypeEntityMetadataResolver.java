package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.repository.core.EntityInformation;

public interface DomainTypeEntityMetadataResolver<T extends DomainTypeEntityMetadata> {

    T resolveEntityMetadata(final Class<?> domainType);

    EntityInformation getEntityInformation(final Class<?> domainType);

}