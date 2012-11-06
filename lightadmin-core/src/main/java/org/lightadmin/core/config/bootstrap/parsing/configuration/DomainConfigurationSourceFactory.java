package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.util.DomainConfigurationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class DomainConfigurationSourceFactory {

	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	public DomainConfigurationSourceFactory( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
	}

	public DomainConfigurationSource createConfigurationSource( Object configurationMetadata ) {
		Assert.notNull( configurationMetadata );

		if ( DomainConfigurationUtils.isConfigurationCandidate( configurationMetadata ) ) {
			final Class configurationMetadataClass = ( Class ) configurationMetadata;
			return domainConfigurationClassSource( configurationMetadataClass );
		}
		if ( configurationMetadata instanceof DomainConfigurationClassDTO ) {
			final DomainConfigurationClassDTO configurationClassDTO = ( DomainConfigurationClassDTO ) configurationMetadata;

			return domainConfigurationClassDTOSource( configurationClassDTO );
		}

		throw new IllegalArgumentException( String.format( "Configuration Metadata of type %s is not supported!", ClassUtils.getDescriptiveType( configurationMetadata ) ) );
	}

	private DomainConfigurationSource domainConfigurationClassDTOSource( final DomainConfigurationClassDTO configurationClassDTO ) {
		final DomainTypeEntityMetadata domainTypeEntityMetadata = entityMetadataResolver.resolveEntityMetadata( configurationClassDTO.getDomainType() );

		return new DomainConfigurationClassDTOSource( domainTypeEntityMetadata, configurationClassDTO );
	}

	private DomainConfigurationSource domainConfigurationClassSource( final Class configurationMetadataClass ) {
		final Class<?> domainType = DomainConfigurationUtils.configurationDomainType( configurationMetadataClass );
		final DomainTypeEntityMetadata domainTypeEntityMetadata = entityMetadataResolver.resolveEntityMetadata( domainType );

		return new DomainConfigurationClassSource( domainTypeEntityMetadata, configurationMetadataClass );
	}
}