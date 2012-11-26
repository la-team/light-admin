package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.field.AbstractFieldMetadata;
import org.lightadmin.core.config.domain.field.CustomFieldMetadata;
import org.lightadmin.core.config.domain.field.PersistentFieldMetadata;
import org.lightadmin.core.config.domain.field.TransientFieldMetadata;
import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

import static org.springframework.util.StringUtils.capitalize;

public class TableListViewConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<ListViewConfigurationUnit> implements ListViewConfigurationUnitBuilder {

	private TableFragment tableFragment;

	private AbstractFieldMetadata currentFieldMetadata = null;

	public TableListViewConfigurationUnitBuilder( Class<?> domainType ) {
		super( domainType );

		this.tableFragment = new TableFragment();
	}

	@Override
	public ListViewConfigurationUnitBuilder field( final String fieldName ) {
		if ( currentFieldMetadata != null ) {
			tableFragment.addField( currentFieldMetadata );
		}
		currentFieldMetadata = new PersistentFieldMetadata( capitalize( fieldName ), fieldName );
		return this;
	}

	@Override
	public ListViewConfigurationUnitBuilder alias( final String alias ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for alias was not specified correctly." );
		}
		currentFieldMetadata.setName( alias );
		return this;
	}

	@Override
	public ListViewConfigurationUnitBuilder attribute( final String fieldName ) {
		if ( currentFieldMetadata != null ) {
			tableFragment.addField( currentFieldMetadata );
		}
		currentFieldMetadata = new TransientFieldMetadata( capitalize( fieldName ), fieldName );
		return this;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public ListViewConfigurationUnitBuilder renderer( final FieldValueRenderer renderer ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for renderer was not specified correctly." );
		}
		currentFieldMetadata = new CustomFieldMetadata( currentFieldMetadata.getName(), renderer );
		return this;
	}

	@Override
	public ListViewConfigurationUnit build() {
		if ( currentFieldMetadata != null ) {
			tableFragment.addField( currentFieldMetadata );
		}

		currentFieldMetadata = null;

		return new FragmentListViewConfigurationUnit( getDomainType(), tableFragment );
	}
}