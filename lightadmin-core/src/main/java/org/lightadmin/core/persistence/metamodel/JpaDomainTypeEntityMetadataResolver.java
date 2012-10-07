package org.lightadmin.core.persistence.metamodel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JpaDomainTypeEntityMetadataResolver implements DomainTypeEntityMetadataResolver<JpaDomainTypeEntityMetadata> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public JpaDomainTypeEntityMetadata resolveEntityMetadata( final Class<?> domainType ) {
		try {
			return new JpaDomainTypeEntityMetadata( entityManager.getMetamodel().entity( domainType ) );
		} catch ( IllegalArgumentException ex ) {
			return null;
		}
	}
}