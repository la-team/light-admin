package org.lightadmin.core.persistence.metamodel;

import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.repository.core.EntityInformation;

import javax.persistence.EntityManager;

public class JpaDomainTypeEntityMetadataResolver implements DomainTypeEntityMetadataResolver<JpaDomainTypeEntityMetadata> {

	private final EntityManager entityManager;

	public JpaDomainTypeEntityMetadataResolver( EntityManager entityManager ) {
		this.entityManager = entityManager;
	}

	@Override
	public JpaDomainTypeEntityMetadata resolveEntityMetadata( final Class<?> domainType ) {
		try {
			return new JpaDomainTypeEntityMetadata( entityManager.getMetamodel().entity( domainType ) );
		} catch ( IllegalArgumentException ex ) {
			return null;
		}
	}

	@Override
	public EntityInformation getEntityInformation( final Class<?> domainType ) {
		return JpaEntityInformationSupport.getMetadata( domainType, entityManager );
	}
}