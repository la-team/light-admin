package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataAware;

public class DefaultEntityMetadataConfigurationUnit extends DomainTypeConfigurationUnit implements EntityMetadataConfigurationUnit, DomainTypeEntityMetadataAware {

	private EntityNameExtractor<?> nameExtractor;

	DefaultEntityMetadataConfigurationUnit( Class<?> domainType, final EntityNameExtractor<?> nameExtractor ) {
		super( domainType );
		this.nameExtractor = nameExtractor;
	}

	@Override
	public EntityNameExtractor getNameExtractor() {
		return nameExtractor != null ? nameExtractor : EntityNameExtractorFactory.forSimpleObject();
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.CONFIGURATION;
	}

	@Override
	public void setDomainTypeEntityMetadata( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		if ( nameExtractor == null ) {
			this.nameExtractor = EntityNameExtractorFactory.forPersistentEntity( domainTypeEntityMetadata );
		}
	}
}