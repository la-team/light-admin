package org.lightadmin.core.config.domain.common;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;

public class PersistentFieldSetConfigurationUnitBuilderAdapter implements PersistentFieldSetConfigurationUnitBuilder {

	private final FieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder;

	public PersistentFieldSetConfigurationUnitBuilderAdapter( Class<?> domainType, DomainConfigurationUnitType configurationUnitType ) {
		this.fieldSetConfigurationUnitBuilder = new GenericFieldSetConfigurationUnitBuilder( domainType, configurationUnitType );
	}

	@Override
	public PersistentFieldSetConfigurationUnitBuilder field( final String fieldName ) {
		fieldSetConfigurationUnitBuilder.field( fieldName );

		return this;
	}

	@Override
	public PersistentFieldSetConfigurationUnitBuilder caption( final String caption ) {
		fieldSetConfigurationUnitBuilder.caption( caption );

		return this;
	}

	@Override
	public FieldSetConfigurationUnit build() {
		return fieldSetConfigurationUnitBuilder.build();
	}
}