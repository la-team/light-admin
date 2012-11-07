package org.lightadmin.core.config.domain.support;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.util.ClassUtils;

public class DomainTypeMetadataAwareConfigurationUnitPostProcessor implements ConfigurationUnitPostProcessor {

	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	public DomainTypeMetadataAwareConfigurationUnitPostProcessor( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
	}

	@Override
	public void postProcess( final ConfigurationUnit configurationUnit ) {
		if ( ClassUtils.isAssignableValue( DomainTypeEntityMetadataAware.class, configurationUnit ) ) {
			final DomainTypeEntityMetadata domainTypeEntityMetadata = entityMetadataResolver.resolveEntityMetadata( configurationUnit.getDomainType() );

			( ( DomainTypeEntityMetadataAware ) configurationUnit ).setDomainTypeEntityMetadata( domainTypeEntityMetadata );
		}
	}
}