package org.lightadmin.core.persistence.metamodel;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class JpaDomainTypeEntityMetadataResolver implements DomainTypeEntityMetadataResolver<JpaDomainTypeEntityMetadata> {

	private final EntityManager entityManager;

	@Autowired
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
}