package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.configuration.support.EntityNameExtractorFactory;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

public class DefaultEntityMetadataConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<EntityMetadataConfigurationUnit> implements EntityMetadataConfigurationUnitBuilder {

	private EntityNameExtractor<?> nameExtractor;

	public DefaultEntityMetadataConfigurationUnitBuilder( Class<?> domainType ) {
		super( domainType );
	}

	@Override
	public EntityMetadataConfigurationUnitBuilder nameField( final String nameField ) {
		this.nameExtractor = EntityNameExtractorFactory.forNamedPersistentEntity( nameField );
		return this;
	}

	@Override
	public EntityMetadataConfigurationUnitBuilder nameExtractor( final EntityNameExtractor<?> nameExtractor ) {
		this.nameExtractor = nameExtractor;
		return this;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public EntityMetadataConfigurationUnit build() {
		return new DefaultEntityMetadataConfigurationUnit( getDomainType(), nameExtractor );
	}
}