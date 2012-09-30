package org.lightadmin.core.view.support;

import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class Filters implements Iterable<Filter> {

	private final List<Filter> filters;

	public Filters( final List<Filter> filters ) {
		Assert.notNull( filters );
		this.filters = newLinkedList( filters );
	}

	@Override
	public Iterator<Filter> iterator() {
		return filters.iterator();
	}
}