package org.lightadmin.core.config.domain.fragment;

import org.lightadmin.core.config.domain.renderer.Renderer;
public class TableFragmentBuilder implements FragmentBuilder {

	private TableFragment tableFragment;

	private String currentFieldName = null;

	public TableFragmentBuilder() {
		this.tableFragment = new TableFragment();
	}

	@Override
	public FragmentBuilder field( final String fieldName ) {
		currentFieldName = fieldName;
		return this;
	}

	@Override
	public FragmentBuilder alias( final String alias ) {
		if ( currentFieldName == null ) {
			throw new RuntimeException( "FieldName for alias was not specified correctly." );
		}
		tableFragment.addColumn( currentFieldName, alias );
		currentFieldName = null;
		return this;
	}

	@Override
	public FragmentBuilder attribute( final String fieldName ) {
		return this;
	}

	@Override
	public FragmentBuilder renderer( final Renderer renderer ) {
		return this;
	}

	@Override
	public Fragment build() {
		if ( currentFieldName != null ) {
			tableFragment.addColumn( currentFieldName, currentFieldName );
		}
		currentFieldName = null;
		return tableFragment;
	}
}