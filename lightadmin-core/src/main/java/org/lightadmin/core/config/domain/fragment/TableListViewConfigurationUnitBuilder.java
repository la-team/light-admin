package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;
import org.lightadmin.core.config.domain.unit.DomainTypeConfigurationUnitBuilder;

public class TableListViewConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<ListViewConfigurationUnit> implements ListViewConfigurationUnitBuilder {

	private TableFragment tableFragment;

	private FieldMetadata currentFieldMetadata = null;

	public TableListViewConfigurationUnitBuilder( Class<?> domainType ) {
		super( domainType );

		this.tableFragment = new TableFragment();
	}

	@Override
	public ListViewConfigurationUnitBuilder field( final String fieldName ) {
		if ( currentFieldMetadata != null ) {
			tableFragment.addField( currentFieldMetadata );
		}
		currentFieldMetadata = new FieldMetadata( fieldName );
		return this;
	}

	@Override
	public ListViewConfigurationUnitBuilder alias( final String alias ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for alias was not specified correctly." );
		}
		currentFieldMetadata.setAlias( alias );
		return this;
	}

	@Override
	public ListViewConfigurationUnitBuilder attribute( final String fieldName ) {
		// Transient/User runtime attributes
		return this;
	}

	@Override
	public ListViewConfigurationUnitBuilder renderer( final FieldValueRenderer renderer ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for renderer was not specified correctly." );
		}
		currentFieldMetadata.setRenderer( renderer );
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