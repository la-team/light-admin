package org.lightadmin.core.config.domain.filter;

import org.lightadmin.core.config.domain.renderer.Renderer;
import org.lightadmin.core.config.domain.support.DomainTypeConfigurationUnitBuilder;

import java.util.Set;

import static com.google.common.collect.Sets.newLinkedHashSet;

public class DefaultFiltersConfigurationUnitBuilder extends DomainTypeConfigurationUnitBuilder<FiltersConfigurationUnit> implements FiltersConfigurationUnitBuilder {

	private Set<FilterMetadata> filtersMetadata = newLinkedHashSet();

	public DefaultFiltersConfigurationUnitBuilder( final Class<?> domainType ) {
		super( domainType );
	}

	@Override
	public FiltersConfigurationUnitBuilder field( final String fieldName ) {
		filtersMetadata.add( FilterMetadataUtils.field( fieldName ) );
		return this;
	}

	@Override
	public FiltersConfigurationUnitBuilder renderer( final Renderer renderer ) {
		return this;
	}

	@Override
	public FiltersConfigurationUnit build() {
		return new DefaultFiltersConfigurationUnit( getDomainType(), filtersMetadata );
	}
}