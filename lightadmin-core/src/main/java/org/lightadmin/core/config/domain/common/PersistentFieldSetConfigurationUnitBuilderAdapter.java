package org.lightadmin.core.config.domain.common;

import javax.servlet.jsp.tagext.SimpleTag;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.view.editor.EnumFieldEditControl;

public class PersistentFieldSetConfigurationUnitBuilderAdapter implements PersistentFieldSetConfigurationUnitBuilder {

	private final GenericFieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder;

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
	public PersistentFieldSetConfigurationUnitBuilder enumeration(String... values) {
		return withCustomEditor(new EnumFieldEditControl(values));
	}

	@Override
	public PersistentFieldSetConfigurationUnitBuilder withCustomEditor( SimpleTag editControl ) {
		fieldSetConfigurationUnitBuilder.withCustomControl( editControl );

		return this;
	}

	@Override
	public FieldSetConfigurationUnit build() {
		return fieldSetConfigurationUnitBuilder.build();
	}
}
