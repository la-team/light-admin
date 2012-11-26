package org.lightadmin.core.config.domain.show;

import org.lightadmin.core.config.domain.field.AbstractFieldMetadata;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

import static org.springframework.util.StringUtils.capitalize;

public class DefaultShowViewConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<ShowViewConfigurationUnit> implements ShowViewConfigurationUnitBuilder {

	private final DefaultShowViewConfigurationUnit configurationUnit;

	private AbstractFieldMetadata currentFieldMetadata;

	public DefaultShowViewConfigurationUnitBuilder( final Class<?> domainType ) {
		super( domainType );

		this.configurationUnit = new DefaultShowViewConfigurationUnit( domainType );
	}

	@Override
	public ShowViewConfigurationUnitBuilder field( final String fieldName ) {
		if ( currentFieldMetadata != null ) {
			configurationUnit.addField( currentFieldMetadata );
		}
		currentFieldMetadata = new PersistentFieldMetadata( capitalize( fieldName ), fieldName );
		return this;
	}

	@Override
	public ShowViewConfigurationUnitBuilder alias( final String alias ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for alias was not specified correctly." );
		}
		currentFieldMetadata.setName( alias );
		return this;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public ShowViewConfigurationUnitBuilder renderer( final FieldValueRenderer renderer ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for renderer was not specified correctly." );
		}
		currentFieldMetadata = new CustomFieldMetadata( currentFieldMetadata.getName(), renderer );
		return this;
	}

	@Override
	public ShowViewConfigurationUnit build() {
		if ( currentFieldMetadata != null ) {
			configurationUnit.addField( currentFieldMetadata );
		}
		return configurationUnit;
	}
}