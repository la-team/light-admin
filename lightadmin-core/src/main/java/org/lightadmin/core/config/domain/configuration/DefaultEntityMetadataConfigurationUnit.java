package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.support.DomainTypeConfigurationUnit;
import org.lightadmin.core.config.domain.support.DomainTypeEntityMetadataAware;
import org.lightadmin.core.config.domain.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.support.EntityNameExtractorFactory;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public class DefaultEntityMetadataConfigurationUnit extends DomainTypeConfigurationUnit implements EntityMetadataConfigurationUnit, DomainTypeEntityMetadataAware {

	private EntityNameExtractor<?> nameExtractor;

	DefaultEntityMetadataConfigurationUnit( Class<?> domainType, final EntityNameExtractor<?> nameExtractor ) {
		super( domainType );
		this.nameExtractor = nameExtractor;
	}

	@Override
	public EntityNameExtractor getNameExtractor() {
		return nameExtractor;
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.CONFIGURATION;
	}

	@Override
	public void setDomainTypeEntityMetadata( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		if ( nameExtractor != null && nameExtractor instanceof EntityNameExtractorFactory.NamedPersistentEntityNameExtractor ) {
			this.nameExtractor = EntityNameExtractorFactory.forPersistentEntity( domainTypeEntityMetadata );
		}
	}
}