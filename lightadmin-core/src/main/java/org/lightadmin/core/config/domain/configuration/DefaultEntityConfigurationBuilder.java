package org.lightadmin.core.config.domain.configuration;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

public class DefaultEntityConfigurationBuilder implements EntityConfigurationBuilder {

	private EntityNameExtractor<?> nameExtractor;

	public DefaultEntityConfigurationBuilder( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		this.nameExtractor = EntityNameExtractorFactory.forPersistentEntity( domainTypeEntityMetadata );
	}

	@Override
	public EntityConfigurationBuilder nameField( final String nameField ) {
		this.nameExtractor = EntityNameExtractorFactory.forNamedPersistentEntity( nameField );
		return this;
	}

	@Override
	public EntityConfigurationBuilder nameExtractor( final EntityNameExtractor<?> nameExtractor ) {
		this.nameExtractor = nameExtractor;
		return this;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public EntityConfiguration build() {
		return new EntityConfiguration( nameExtractor );
	}
}