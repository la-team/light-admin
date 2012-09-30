package org.lightadmin.core.view.support;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DefaultFilterBuilder implements FilterBuilder {

	private List<Filter> filters = newLinkedList();

	public DefaultFilterBuilder( ) {
	}

	@Override
	public FilterBuilder field( final String fieldName ) {
		filters.add( FilterUtils.field( fieldName ) );
		return this;
	}

	@Override
	public FilterBuilder renderer( final Renderer renderer ) {
		return this;
	}

	@Override
	public Filters build() {
		return new Filters( filters );
	}
}