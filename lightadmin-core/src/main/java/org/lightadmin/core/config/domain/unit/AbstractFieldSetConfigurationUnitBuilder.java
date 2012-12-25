package org.lightadmin.core.config.domain.unit;

import static org.springframework.util.StringUtils.capitalize;

import org.lightadmin.core.config.domain.field.AbstractFieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;

public abstract class AbstractFieldSetConfigurationUnitBuilder<U extends FieldSetConfigurationUnit, B extends AbstractFieldSetConfigurationUnitBuilder<?, ?>>
		extends DomainTypeConfigurationUnitBuilder<U> {

	private final U configurationUnit;
	private AbstractFieldMetadata currentFieldMetadata;

	public AbstractFieldSetConfigurationUnitBuilder(Class<?> domainType, U configurationUnit) {
		super(domainType);
		this.configurationUnit = configurationUnit;
	}

	@SuppressWarnings("unchecked")
	public B field( final String fieldName ) {
		if ( currentFieldMetadata != null ) {
			configurationUnit.addField( currentFieldMetadata );
		}
		currentFieldMetadata = new PersistentFieldMetadata( capitalize( fieldName ), fieldName );
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B alias( final String alias ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for alias was not specified correctly." );
		}
		currentFieldMetadata.setName( alias );
		return (B) this;
	}

	@Override
	public U build() {
		if ( currentFieldMetadata != null ) {
			configurationUnit.addField( currentFieldMetadata );
		}
		return configurationUnit;
	}
}
