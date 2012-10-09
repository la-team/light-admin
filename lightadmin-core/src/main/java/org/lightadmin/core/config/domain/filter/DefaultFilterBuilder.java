package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.domain.renderer.Renderer;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DefaultFilterBuilder implements FilterBuilder {

	private final List<Filter> filters = newLinkedList();

	public DefaultFilterBuilder() {
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