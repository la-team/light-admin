package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.beans.parsing.ConfigurationUnitPropertyFilter;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class Filters implements Iterable<Filter> {

	private final List<Filter> filters;

	Filters( final List<Filter> filters ) {
		Assert.notNull( filters );
		this.filters = newLinkedList( filters );
	}

	@Override
	public Iterator<Filter> iterator() {
		return filters.iterator();
	}

	public int size() {
		return filters.size();
	}

	public Filters filter( ConfigurationUnitPropertyFilter propertyFilter ) {
		List<Filter> result = newLinkedList();
		for ( Filter filter : filters ) {
			if ( propertyFilter.apply( filter.getFieldName() ) ) {
				result.add( filter );
			}
		}
		return new Filters( result );
	}
}