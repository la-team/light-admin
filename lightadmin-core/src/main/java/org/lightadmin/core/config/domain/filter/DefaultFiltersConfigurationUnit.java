package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DefaultFiltersConfigurationUnit implements FiltersConfigurationUnit {

	private final List<FilterMetadata> filtersMetadata;

	DefaultFiltersConfigurationUnit( final List<FilterMetadata> filtersMetadata ) {
		Assert.notNull( filtersMetadata );
		this.filtersMetadata = newLinkedList( filtersMetadata );
	}

	@Override
	public Iterator<FilterMetadata> iterator() {
		return filtersMetadata.iterator();
	}

	public int size() {
		return filtersMetadata.size();
	}

	@Override
	public DomainConfigurationUnitType getDomainConfigurationUnitType() {
		return DomainConfigurationUnitType.FILTERS;
	}
}