package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.domain.renderer.Renderer;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;

public class DefaultFiltersConfigurationUnitBuilder implements FiltersConfigurationUnitBuilder {

	private List<FilterMetadata> filtersMetadata = newLinkedList();

	@Override
	public FiltersConfigurationUnitBuilder field( final String fieldName ) {
//		filtersMetadata.add( FilterUtils.field( fieldName ) );
		return this;
	}

	@Override
	public FiltersConfigurationUnitBuilder renderer( final Renderer renderer ) {
		return this;
	}

	@Override
	public FiltersConfigurationUnit build() {
		return new DefaultFiltersConfigurationUnit( filtersMetadata );
	}
}