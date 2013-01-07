package org.lightadmin.core.config.domain.common;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.field.FieldMetadata;
import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.DefaultFieldSetConfigurationUnit;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.FieldSetConfigurationUnit;

import static org.lightadmin.core.config.domain.field.FieldMetadataFactory.*;
import static org.springframework.util.StringUtils.capitalize;

public class GenericFieldSetConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<FieldSetConfigurationUnit> implements FieldSetConfigurationUnitBuilder {

	private final FieldSetConfigurationUnit configurationUnit;

	private FieldMetadata currentFieldMetadata;

	public GenericFieldSetConfigurationUnitBuilder( Class<?> domainType, DomainConfigurationUnitType configurationUnitType ) {
		super( domainType );

		this.configurationUnit = new DefaultFieldSetConfigurationUnit( domainType, configurationUnitType );
	}

	@Override
	public FieldSetConfigurationUnitBuilder field( final String fieldName ) {
		addCurrentFieldToUnit();

		currentFieldMetadata = persistentField( capitalize( fieldName ), fieldName );

		return this;
	}

	@Override
	public FieldSetConfigurationUnitBuilder caption( final String caption ) {
		assertFieldMetadataIsNull();

		currentFieldMetadata.setName( caption );

		return this;
	}

	@Override
	public FieldSetConfigurationUnitBuilder dynamic( final String expression ) {
		addCurrentFieldToUnit();

		currentFieldMetadata = transientField( "Undefined", expression );

		return this;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public FieldSetConfigurationUnitBuilder renderable( final FieldValueRenderer renderer ) {
		addCurrentFieldToUnit();

		currentFieldMetadata = customField( "Undefined", renderer );

		return this;
	}

	@Override
	public FieldSetConfigurationUnit build() {
		addCurrentFieldToUnit();

		return configurationUnit;
	}

	private void addCurrentFieldToUnit() {
		if ( currentFieldMetadata != null ) {
			configurationUnit.addField( currentFieldMetadata );
		}
	}

	private void assertFieldMetadataIsNull() {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "Field is not defined yet." );
		}
	}
}