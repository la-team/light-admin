package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnit;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataAware;

public class DefaultEntityMetadataConfigurationUnit extends DomainTypeConfigurationUnit implements EntityMetadataConfigurationUnit, DomainTypeEntityMetadataAware {

	private EntityNameExtractor<?> nameExtractor;

	private final String singularName;
	private final String pluralName;

	DefaultEntityMetadataConfigurationUnit( Class<?> domainType, final EntityNameExtractor<?> nameExtractor, final String singularName, final String pluralName ) {
		super( domainType );
		this.nameExtractor = nameExtractor;
		this.singularName = singularName;
		this.pluralName = pluralName;
	}

	@Override
	public EntityNameExtractor getNameExtractor() {
		return nameExtractor;
	}

	@Override
	public String getSingularName() {
		return this.singularName;
	}

	@Override
	public String getPluralName() {
		return this.pluralName;
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.CONFIGURATION;
	}

	@Override
	public void setDomainTypeEntityMetadata( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		if ( nameExtractor == null ) {
			this.nameExtractor = EntityNameExtractorFactory.forPersistentEntity( this.singularName, domainTypeEntityMetadata );
		}
	}
}