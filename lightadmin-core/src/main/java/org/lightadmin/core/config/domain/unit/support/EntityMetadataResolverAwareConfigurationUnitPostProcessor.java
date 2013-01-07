package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;

public abstract class EntityMetadataResolverAwareConfigurationUnitPostProcessor implements ConfigurationUnitPostProcessor {

	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	public EntityMetadataResolverAwareConfigurationUnitPostProcessor( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
	}


	DomainTypeEntityMetadata<DomainTypeAttributeMetadata> resolveEntityMetadata( ConfigurationUnit configurationUnit ) {
		return resolveEntityMetadata( configurationUnit.getDomainType() );
	}

	@SuppressWarnings( "unchecked" )
	DomainTypeEntityMetadata<DomainTypeAttributeMetadata> resolveEntityMetadata( Class<?> domainType ) {
		return entityMetadataResolver.resolveEntityMetadata( domainType );
	}
}