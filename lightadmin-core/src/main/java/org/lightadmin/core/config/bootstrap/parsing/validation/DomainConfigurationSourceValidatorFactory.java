package org.lightadmin.core.config.bootstrap.parsing.validation;

import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;

public class DomainConfigurationSourceValidatorFactory {

	private final DomainTypeEntityMetadataResolver entityMetadataResolver;

	public DomainConfigurationSourceValidatorFactory( final DomainTypeEntityMetadataResolver entityMetadataResolver ) {
		this.entityMetadataResolver = entityMetadataResolver;
	}

	public DomainConfigurationSourceValidator getValidator() {
		return new DomainConfigurationUnitsSourceValidator( entityMetadataResolver );
	}

}