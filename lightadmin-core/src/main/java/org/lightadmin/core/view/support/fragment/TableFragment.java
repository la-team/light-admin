package org.lightadmin.core.view.support.fragment;

import org.lightadmin.core.util.Pair;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class TableFragment implements Fragment {

	private Set<Pair<String, String>> columns = newLinkedHashSet();

	public void addColumn( String fieldName, String alias ) {
		columns.add( Pair.stringPair( fieldName, alias ) );
	}

	public Set<Pair<String, String>> getColumns() {
		return columns;
	}
}