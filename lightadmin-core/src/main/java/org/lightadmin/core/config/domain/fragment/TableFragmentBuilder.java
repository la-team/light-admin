package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.renderer.FieldValueRenderer;

public class TableFragmentBuilder implements FragmentBuilder {

	private TableFragment tableFragment;

	private FieldMetadata currentFieldMetadata = null;

	public TableFragmentBuilder() {
		this.tableFragment = new TableFragment();
	}

	@Override
	public FragmentBuilder field( final String fieldName ) {
		if ( currentFieldMetadata != null ) {
			tableFragment.addField( currentFieldMetadata );
		}
		currentFieldMetadata = new FieldMetadata( fieldName );
		return this;
	}

	@Override
	public FragmentBuilder alias( final String alias ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for alias was not specified correctly." );
		}
		currentFieldMetadata.setAlias( alias );
		return this;
	}

	@Override
	public FragmentBuilder attribute( final String fieldName ) {
		// Transient/User runtime attributes
		return this;
	}

	@Override
	public FragmentBuilder renderer( final FieldValueRenderer renderer ) {
		if ( currentFieldMetadata == null ) {
			throw new RuntimeException( "FieldName for renderer was not specified correctly." );
		}
		currentFieldMetadata.setRenderer( renderer );
		return this;
	}

	@Override
	public TableFragment build() {
		if ( currentFieldMetadata != null ) {
			tableFragment.addField( currentFieldMetadata );
		}
		currentFieldMetadata = null;
		return tableFragment;
	}
}