package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.config.domain.support.DomainTypeConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.support.EntityNameExtractor;
import org.lightadmin.core.config.domain.support.EntityNameExtractorFactory;

public class DefaultEntityMetadataConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<EntityMetadataConfigurationUnit> implements EntityMetadataConfigurationUnitBuilder {

	private EntityNameExtractor<?> nameExtractor;

	public DefaultEntityMetadataConfigurationUnitBuilder( Class<?> domainType ) {
		super( domainType );
		this.nameExtractor = EntityNameExtractorFactory.forSimpleObject();
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