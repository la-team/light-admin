package org.lightadmin.core.config.bootstrap.parsing.configuration;

import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.config.domain.unit.ConfigurationUnitsConverter;
import org.lightadmin.core.config.domain.unit.support.ConfigurationUnitPostProcessor;
import org.lightadmin.core.config.domain.unit.support.DomainTypeMetadataAwareConfigurationUnitPostProcessor;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.lightadmin.core.util.DomainConfigurationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class DomainConfigurationSourceFactory {

	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	private final ConfigurationUnitPostProcessor configurationUnitPostProcessor;

	public DomainConfigurationSourceFactory( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this( entityMetadataResolver, new DomainTypeMetadataAwareConfigurationUnitPostProcessor( entityMetadataResolver ) );
	}

	public DomainConfigurationSourceFactory( final DomainTypeEntityMetadataResolver entityMetadataResolver, final ConfigurationUnitPostProcessor configurationUnitPostProcessor ) {

		this.entityMetadataResolver = entityMetadataResolver;
		this.configurationUnitPostProcessor = configurationUnitPostProcessor;
	}

	public DomainConfigurationSource createConfigurationSource( Object configurationMetadata ) {
		Assert.notNull( configurationMetadata );

		if ( DomainConfigurationUtils.isConfigurationCandidate( configurationMetadata ) ) {
			final Class configurationMetadataClass = ( Class ) configurationMetadata;

			final ConfigurationUnits configurationUnits = ConfigurationUnitsConverter.fromConfiguration( configurationMetadataClass );

			return domainConfigurationUnitsSource( configurationUnits );
		}

		if ( configurationMetadata instanceof ConfigurationUnits ) {
			ConfigurationUnits configurationUnits = ( ConfigurationUnits ) configurationMetadata;

			return domainConfigurationUnitsSource( configurationUnits );
		}

		throw new IllegalArgumentException( String.format( "Configuration Metadata of type %s is not supported!", ClassUtils.getDescriptiveType( configurationMetadata ) ) );
	}

	private DomainConfigurationSource domainConfigurationUnitsSource( final ConfigurationUnits configurationUnits ) {
		final DomainTypeEntityMetadata domainTypeEntityMetadata = entityMetadataResolver.resolveEntityMetadata( configurationUnits.getDomainType() );

		for ( ConfigurationUnit configurationUnit : configurationUnits ) {
			configurationUnitPostProcessor.postProcess( configurationUnit );
		}

		return new DomainConfigurationUnitsSource( domainTypeEntityMetadata, configurationUnits );
	}
}