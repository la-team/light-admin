package org.lightadmin.core.persistence.metamodel;

public interface DomainTypeEntityMetadataResolver<T extends DomainTypeEntityMetadata> {

	T resolveEntityMetadata( final Class<?> domainType );

}