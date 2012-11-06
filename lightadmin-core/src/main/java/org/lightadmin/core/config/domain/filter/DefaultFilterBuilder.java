package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.domain.renderer.Renderer;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DefaultFilterBuilder implements FilterBuilder {

	private final List<Filter> filters = newLinkedList();

	private DomainTypeEntityMetadata domainTypeEntityMetadata;

	public DefaultFilterBuilder() {
	}

	public DefaultFilterBuilder( final DomainTypeEntityMetadata domainTypeEntityMetadata ) {
		this.domainTypeEntityMetadata = domainTypeEntityMetadata;
	}

	@Override
	public FilterBuilder field( final String fieldName ) {
		final Filter fieldFilter = FilterUtils.field( fieldName );
		filters.add( fieldFilter );
		return this;
	}

	@Override
	public FilterBuilder renderer( final Renderer renderer ) {
		return this;
	}

	@Override
	public Filters build() {
		for ( Filter filter : filters ) {
			filter.setAttributeMetadata( domainTypeEntityMetadata.getAttribute( filter.getFieldName() ) );
		}
		return new Filters( filters );
	}
}